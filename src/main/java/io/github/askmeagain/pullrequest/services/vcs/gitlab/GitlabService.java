package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.dto.gitlab.comment.GitlabMergeRequestCommentRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy.Change;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.GitlabDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.Position;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.project.GitlabProjectResponse;
import io.github.askmeagain.pullrequest.services.StateService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.xml.transform.sax.SAXTransformerFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public final class GitlabService implements VcsService {

  @Getter(lazy = true)
  private final PullrequestPluginState state = StateService.getInstance().getState();

  public static GitlabService getInstance() {
    return ApplicationManager.getApplication().getService(GitlabService.class);
  }

  @SneakyThrows
  public List<MergeRequest> getMergeRequests(String projectId, ConnectionConfig connection) {
    return getGitlabMergeRequests(projectId, connection)
        .stream()
        .map(pr -> MergeRequest.builder()
            .id(pr.getIid() + "")
            .name(pr.getTitle())
            .sourceBranch(pr.getSource_branch())
            .targetBranch(pr.getTarget_branch())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  public void addCommentToThread(String projectId, ConnectionConfig connection, String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request) {
    new GitlabRestClient(connection).addCommentToThread(projectId, mergeRequestId, discussionId, request);
  }

  @Override
  public List<String> getFilesOfPr(String projectId, ConnectionConfig connection, String mergeRequestId) {
    if (Boolean.parseBoolean(connection.getConfigs().get("legacy_gitlab"))) {
      return new GitlabRestClient(connection)
          .getMergerequestDiffLegacy(projectId, mergeRequestId)
          .getChanges()
          .stream()
          .map(Change::getNew_path)
          .collect(Collectors.toList());
    }

    return new GitlabRestClient(connection)
        .getMergeRequestDiff(projectId, mergeRequestId).stream()
        .map(GitlabMergeRequestFileDiff::getNew_path)
        .collect(Collectors.toList());
  }

  @Override
  public List<MergeRequestDiscussion> getCommentsOfPr(String projectId, ConnectionConfig connection, String mergeRequestId) {
    return getDiscussionsOfPr(projectId, connection, mergeRequestId)
        .stream()
        .map(discussion -> {
          var n = discussion.getNotes().get(0);
          var isNotSource = n.getPosition().getOld_line() != null;

          return MergeRequestDiscussion.builder()
              .discussionId(discussion.getId())
              .isSourceDiscussion(n.getPosition().getOld_line() == null)
              .line(isNotSource ? n.getPosition().getOld_line() - 1 : n.getPosition().getNew_line() - 1)
              .reviewComments(discussion.getNotes().stream()
                  .map(note -> ReviewComment.builder()
                      .text(note.getBody())
                      .author(note.getAuthor().getName())
                      .build())
                  .collect(Collectors.toList()))
              .build();
        })
        .collect(Collectors.toList());
  }

  @Override
  public String getFileOfBranch(String projectId, ConnectionConfig connection, String branch, String filePath) {
    return new GitlabRestClient(connection).getFileOfBranch(projectId, filePath, branch);
  }

  @Override
  public void addMergeRequestComment(String projectId, ConnectionConfig connection, String mergeRequestId, CommentRequest comment) {
    var diffVersion = new GitlabRestClient(connection).getDiffVersion(projectId, mergeRequestId).get(0);

    var request = GitlabMergeRequestCommentRequest.builder()
        .body(comment.getText())
        .position(Position.builder()
            .position_type("text")
            .base_sha(diffVersion.getBase_commit_sha())
            .head_sha(diffVersion.getHead_commit_sha())
            .start_sha(diffVersion.getStart_commit_sha())
            .new_path(comment.getNewFileName())
            .old_path(comment.getOldFileName())
            .old_line(comment.isSourceComment() ? comment.getLine() + 1 : null)
            .new_line(comment.isSourceComment() ? null : comment.getLine() + 1)
            .build())
        .build();

    new GitlabRestClient(connection).addMergeRequestComment(projectId, mergeRequestId, request);
  }

  public GitlabProjectResponse getProject(ConnectionConfig connection, String projectId) {
    return new GitlabRestClient(connection).getProject(projectId);
  }

  @SneakyThrows
  private List<GitlabDiscussionResponse> getDiscussionsOfPr(String projectId, ConnectionConfig connection, String mergeRequestId) {
    return new GitlabRestClient(connection).getDiscussions(projectId, mergeRequestId);
  }

  @SneakyThrows
  private List<GitlabMergeRequestResponse> getGitlabMergeRequests(String projectId, ConnectionConfig connection) {
    return new GitlabRestClient(connection).getMergeRequests(projectId);
  }
}
