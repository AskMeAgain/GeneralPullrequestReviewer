package io.github.askmeagain.pullrequest.services.vcs.github;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.dto.github.comment.GithubMergeRequestCommentRequest;
import io.github.askmeagain.pullrequest.dto.github.diffs.GithubDiffResponse;
import io.github.askmeagain.pullrequest.dto.github.discussionnote.GithubAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.dto.github.discussions.GithubDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.github.mergerequest.Assignee;
import io.github.askmeagain.pullrequest.services.PasswordService;
import io.github.askmeagain.pullrequest.services.StateService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.VcsServiceProgressionProxy;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.stream.Collectors;

@Service
public final class GithubService implements VcsService {

  @Getter(lazy = true)
  private final PullrequestPluginState state = StateService.getInstance().getState();

  private final Map<String, GithubApi> apisPerConnection = new HashMap<>();

  private final PasswordService passwordService = PasswordService.getInstance();

  public static VcsService getInstance() {
    return new VcsServiceProgressionProxy(ApplicationManager.getApplication().getService(GithubService.class));
  }

  private GithubApi getOrCreateApi(ConnectionConfig connection) {
    var name = connection.getName();

    if (!apisPerConnection.containsKey(name)) {
      var url = String.format("%s%s",
          connection.getConfigs().get("githubUrl"),
          connection.getConfigs().get("userName")
      );

      var api = Feign.builder()
          .requestInterceptor(template -> template.header("Authorization", "Bearer " + getToken(connection)))
          .client(new OkHttpClient())
          .encoder(new JacksonEncoder())
          .decoder(new JacksonDecoder())
          .target(GithubApi.class, url);
      apisPerConnection.put(name, api);
    }

    return apisPerConnection.get(name);
  }

  @Override
  public List<MergeRequest> getMergeRequests(String projectId, ConnectionConfig connectionName) {
    return getOrCreateApi(connectionName).getMergeRequests(projectId).stream()
        .map(mr -> MergeRequest.builder()
            //TODO switch to reviewers
            .url(mr.getHtml_url())
            .commitSha(mr.getHead().getSha())
            .reviewer(mr.getAssignees().stream().map(Assignee::getAvatar_url).collect(Collectors.toList()))
            .targetBranch(mr.getBase().getRef())
            .sourceBranch(mr.getHead().getRef())
            .id(mr.getNumber() + "")
            .name(mr.getTitle())
            .approved(true)
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public void addCommentToThread(
      String projectId,
      ConnectionConfig connectionName,
      String mergeRequestId,
      String discussionId,
      String body
  ) {
    getOrCreateApi(connectionName).addCommentToThread(
        GithubAddCommentToDiscussionRequest.builder()
            .body(body)
            .build(),
        projectId,
        mergeRequestId,
        discussionId
    );
  }

  @Override
  public void editComment(ConnectionConfig connectionName, String projectId, String mergeRequestId, String discussionId, String note_id, String body) {
    getOrCreateApi(connectionName).editComment(
        projectId,
        note_id,
        GithubAddCommentToDiscussionRequest.builder()
            .body(body)
            .build()
    );
  }

  @Override
  public void deleteComment(ConnectionConfig connectionName, String projectId, String mergeRequestId, String discussionId, String note_id) {
    getOrCreateApi(connectionName).deleteComment(projectId, discussionId);
  }

  @Override
  public List<String> getFilesOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId) {
    return getOrCreateApi(connectionName).getMergerequestDiff(projectId, mergeRequestId).stream()
        .map(GithubDiffResponse::getFilename)
        .collect(Collectors.toList());
  }

  @Override
  public List<MergeRequestDiscussion> getCommentsOfPr(
      String projectId,
      ConnectionConfig connectionName,
      String mergeRequestId,
      String filePath
  ) {
    var discussions = getOrCreateApi(connectionName).getDiscussions(projectId, mergeRequestId);

    var replyMap = discussions.stream()
        .filter(x -> x.getIn_reply_to_id() != null)
        .collect(Collectors.groupingBy(GithubDiscussionResponse::getIn_reply_to_id));

    return discussions.stream()
        .filter(x -> x.getPath().equals(filePath))
        .filter(x -> x.getIn_reply_to_id() == null)
        .map(discussion -> MergeRequestDiscussion.builder()
            .url(discussion.getHtml_url())
            .startLine((discussion.getStart_line() == null ? discussion.getLine() : discussion.getStart_line()) - 1)
            .endLine(discussion.getLine() - 1)
            .isSourceDiscussion(discussion.getSide().equals("LEFT"))
            .discussionId(discussion.getId() + "")
            .reviewComment(ReviewComment.builder()
                .text(discussion.getBody())
                .discussionId(discussion.getId() + "")
                .author(discussion.getUser().getLogin())
                .build())
            .reviewComments(replyMap.getOrDefault(discussion.getId() + "", Collections.emptyList()).stream()
                .map(review -> ReviewComment.builder()
                    .text(review.getBody())
                    .discussionId(review.getIn_reply_to_id())
                    .noteId(review.getId() + "")
                    .author(discussion.getUser().getLogin())
                    .build())
                .collect(Collectors.toList()))
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public FileResponse getFileOfBranch(String projectId, ConnectionConfig connectionName, String branch, String filePath) {
    var encodedFilePath = encodePath(filePath);

    var response = getOrCreateApi(connectionName).getFileOfBranch(projectId, encodedFilePath, branch);

    return FileResponse.builder()
        .fileContent(new String(Base64.getDecoder().decode(response.getContent().replaceAll("\n", ""))))
        .commitId(response.getSha())
        .build();
  }

  @SneakyThrows
  public String getDiffHunk(
      String projectId,
      ConnectionConfig connection,
      String mergeRequestId
  ) {
    var response = getOrCreateApi(connection).getDiffHunksOfMergeRequest(projectId, mergeRequestId);
    return new String(response.body().asInputStream().readAllBytes());
  }

  @Override
  public void addMergeRequestComment(
      String projectId,
      ConnectionConfig connectionName,
      String mergeRequestId,
      CommentRequest comment
  ) {
    if (isWithinReach(comment)) {
      System.out.println("This is sadly not possible right now");
      return;
    }

    //TODO
    var startLine = !Objects.equals(comment.getLineStart(), comment.getLineEnd()) ? comment.getLineStart() : null;

    getOrCreateApi(connectionName).addMergeRequestComment(
        GithubMergeRequestCommentRequest.builder()
            .commit_id(comment.getCommitId())
            .body(comment.getText())
            .line(comment.getLineEnd() + 1)
            .start_line(startLine + 1)
            .side(comment.isSourceComment() ? "LEFT" : "RIGHT")
            .path(comment.isSourceComment() ? comment.getOldFileName() : comment.getNewFileName())
            .build(),
        projectId,
        mergeRequestId
    );
  }

  private boolean isWithinReach(CommentRequest comment) {
    var diffHunk = comment.getHunk();
    if (comment.isSourceComment()) {
      return diffHunk.getFirstLineSource() < comment.getLineStart() && comment.getLineEnd() < diffHunk.getLastLineSource();
    }
    return diffHunk.getFirstLineTarget() < comment.getLineStart() && comment.getLineEnd() < diffHunk.getLastLineTarget();
  }

  public void approveMergeRequest(String projectId, ConnectionConfig connection, String mergeRequestId) {
    getOrCreateApi(connection).approveMergeRequest(projectId, mergeRequestId);
  }

  public void ping(ConnectionConfig connection, String token) {
    var url = String.format("%s%s",
        connection.getConfigs().get("githubUrl"),
        connection.getConfigs().get("userName")
    );

    var api = Feign.builder()
        .requestInterceptor(template -> template.header("Authorization", "Bearer " + token))
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder())
        .decoder(new JacksonDecoder())
        .target(GithubApi.class, url);
    api.getProject(connection.getConfigs().get("repoName"));
  }

  public ProjectResponse getProject(ConnectionConfig connection, String projectId) {
    var response = getOrCreateApi(connection).getProject(projectId);
    return ProjectResponse.builder()
        .projectId(response.getId() + "")
        .url(response.getHtml_url())
        .name(response.getName())
        .build();
  }

  private String getToken(ConnectionConfig connection) {
    return passwordService.getPassword(connection.getName());
  }

  public static String encodePath(String path) {
    return path
        .replaceAll("/", "%2F")
        .replaceAll(" ", "%20")
        .replaceAll("-", "%2D");
  }
}
