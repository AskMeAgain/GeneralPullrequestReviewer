package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.github.askmeagain.pullrequest.PluginUtils;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.dto.gitlab.comment.GitlabMergeRequestCommentRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy.Change;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.*;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.Reviewer;
import io.github.askmeagain.pullrequest.services.PasswordService;
import io.github.askmeagain.pullrequest.services.StateService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.VcsServiceProgressionProxy;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.stream.Collectors;

@Service
public final class GitlabService implements VcsService {

  @Getter(lazy = true)
  private final PullrequestPluginState state = StateService.getInstance().getState();

  private final Map<String, GitlabApi> apisPerConnection = new HashMap<>();

  private final PasswordService passwordService = PasswordService.getInstance();

  public static VcsService getInstance() {
    return new VcsServiceProgressionProxy(ApplicationManager.getApplication().getService(GitlabService.class));
  }

  private GitlabApi getOrCreateApi(ConnectionConfig connection) {
    var name = connection.getName();

    if (!apisPerConnection.containsKey(name)) {
      var api = Feign.builder()
          .requestInterceptor(template -> template.query("private_token", getToken(connection)))
          .client(new OkHttpClient())
          .encoder(new JacksonEncoder())
          .decoder(new JacksonDecoder())
          .target(GitlabApi.class, connection.getConfigs().get("gitlabUrl"));
      apisPerConnection.put(name, api);
    }

    return apisPerConnection.get(name);
  }

  @SneakyThrows
  public List<MergeRequest> getMergeRequests(String projectId, ConnectionConfig connection) {
    return getGitlabMergeRequests(projectId, connection)
        .stream()
        .map(pr -> MergeRequest.builder()
            .reviewer(pr.getReviewers().stream().map(Reviewer::getAvatar_url).collect(Collectors.toList()))
            .approved(pr.merge_status.equals("can_be_merged"))
            .id(pr.getIid() + "")
            .name(pr.getTitle())
            .sourceBranch(pr.getSource_branch())
            .targetBranch(pr.getTarget_branch())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public void addCommentToThread(
      String projectId,
      ConnectionConfig connection,
      String mergeRequestId,
      String discussionId,
      String text
  ) {
    getOrCreateApi(connection).addCommentToThread(
        GitlabAddCommentToDiscussionRequest.builder()
            .body(text)
            .build(),
        projectId,
        mergeRequestId,
        discussionId
    );
  }

  @Override
  public void editComment(
      ConnectionConfig connection,
      String projectId,
      String mergeRequestId,
      String discussionId,
      String note_id,
      String body
  ) {
    getOrCreateApi(connection).editComment(projectId, mergeRequestId, discussionId, note_id, body);
  }

  @Override
  public void deleteComment(
      ConnectionConfig connection,
      String projectId,
      String mergeRequestId,
      String discussionId,
      String note_id
  ) {
    getOrCreateApi(connection).deleteComment(projectId, mergeRequestId, discussionId, note_id);
  }

  @Override
  public List<String> getFilesOfPr(String projectId, ConnectionConfig connection, String mergeRequestId) {
    if (Boolean.parseBoolean(connection.getConfigs().get("legacy_gitlab"))) {
      return getOrCreateApi(connection)
          .getMergerequestDiffLegacy(projectId, mergeRequestId)
          .getChanges()
          .stream()
          .map(Change::getNew_path)
          .collect(Collectors.toList());
    }

    return getOrCreateApi(connection)
        .getMergerequestDiff(projectId, mergeRequestId)
        .stream()
        .map(GitlabMergeRequestFileDiff::getNew_path)
        .collect(Collectors.toList());
  }

  @Override
  public List<MergeRequestDiscussion> getCommentsOfPr(
      String projectId,
      ConnectionConfig connection,
      String mergeRequestId,
      String file
  ) {
    return getDiscussionsOfPr(projectId, connection, mergeRequestId)
        .stream()
        .map(discussion -> {
          var n = discussion.getNotes().get(0);
          if (n.getPosition() == null) {
            return null;
          }

          if (!Objects.equals(n.getPosition().getNew_path(), file)) {
            return null;
          }

          var isNotSource = n.getPosition().getOld_line() != null;
          var start = Optional.of(n.getPosition())
              .map(Position::getLine_range)
              .map(LineRange::getStart)
              .orElse(Start.builder()
                  .old_line(n.getPosition().getOld_line())
                  .new_line(n.getPosition().getNew_line())
                  .build());

          var end = Optional.of(n.getPosition())
              .map(Position::getLine_range)
              .map(LineRange::getEnd)
              .orElse(End.builder()
                  .old_line(n.getPosition().getOld_line())
                  .new_line(n.getPosition().getNew_line())
                  .build());

          return MergeRequestDiscussion.builder()
              .discussionId(discussion.getId())
              .isSourceDiscussion(n.getPosition().getOld_line() == null)
              .startLine(isNotSource ? start.getOld_line() - 1 : start.getNew_line() - 1)
              .endLine(isNotSource ? end.getOld_line() - 1 : end.getNew_line() - 1)
              .reviewComments(discussion.getNotes().stream()
                  .map(note -> ReviewComment.builder()
                      .noteId(note.getId())
                      .text(note.getBody())
                      .author(note.getAuthor().getName())
                      .build())
                  .collect(Collectors.toList()))
              .build();
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public FileResponse getFileOfBranch(String projectId, ConnectionConfig connection, String branch, String filePath) {
    var encodedFilePath = PluginUtils.encodePath(filePath);

    var response = getOrCreateApi(connection).getFileOfBranch(projectId, encodedFilePath, branch);

    return FileResponse.builder()
        .fileContent(new String(Base64.getDecoder().decode(response.get("content"))))
        .commitId(response.get("commit_id"))
        .build();
  }

  @Override
  public void addMergeRequestComment(String projectId, ConnectionConfig connection, String mergeRequestId, CommentRequest comment) {
    var diffVersion = getOrCreateApi(connection).getDiffVersion(projectId, mergeRequestId).get(0);

    var request = GitlabMergeRequestCommentRequest.builder()
        .body(comment.getText())
        .position(Position.builder()
            .position_type("text")
            .base_sha(diffVersion.getBase_commit_sha())
            .head_sha(diffVersion.getHead_commit_sha())
            .start_sha(diffVersion.getStart_commit_sha())
            .new_path(comment.getNewFileName())
            .old_path(comment.getOldFileName())
            .old_line(comment.isSourceComment() ? comment.getLineEnd() + 1 : null)
            .new_line(comment.isSourceComment() ? null : comment.getLineEnd() + 1)
            .build())
        .build();

    getOrCreateApi(connection).addMergeRequestComment(request, projectId, mergeRequestId);
  }

  public ProjectResponse getProject(ConnectionConfig connection, String projectId) {
    var response = getOrCreateApi(connection).getProject(projectId);
    return ProjectResponse.builder()
        .name(response.getName())
        .build();
  }

  @SneakyThrows
  private List<GitlabDiscussionResponse> getDiscussionsOfPr(String projectId, ConnectionConfig connection, String mergeRequestId) {
    return getOrCreateApi(connection).getDiscussions(projectId, mergeRequestId);
  }

  @SneakyThrows
  private List<GitlabMergeRequestResponse> getGitlabMergeRequests(String projectId, ConnectionConfig connection) {
    return getOrCreateApi(connection).getMergeRequests(projectId);
  }

  public void approveMergeRequest(String projectId, ConnectionConfig connection, String mergeRequestId) {
    getOrCreateApi(connection).approveMergeRequest(projectId, mergeRequestId);
  }

  private String getToken(ConnectionConfig connection) {
    return passwordService.getPassword(connection.getName());
  }

}
