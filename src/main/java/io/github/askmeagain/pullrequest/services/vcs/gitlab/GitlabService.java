package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.dto.gitlab.comment.GitlabMergeRequestCommentRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.GitlabDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.Position;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.project.GitlabProjectResponse;
import io.github.askmeagain.pullrequest.services.StateService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import lombok.Getter;
import lombok.SneakyThrows;

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
  public List<MergeRequest> getMergeRequests(ConnectionConfig connection) {
    return getGitlabMergeRequests(connection)
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
  public void addCommentToThread(ConnectionConfig connection, String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request) {
    var projectId = connection.getConfigs().get("projects");

    new GitlabRestClient(connection).addCommentToThread(projectId, mergeRequestId, discussionId, request);
  }

  @Override
  public List<String> getFilesOfPr(ConnectionConfig connection, String mergeRequestId) {
    var projectId = connection.getConfigs().get("projects");
    return new GitlabRestClient(connection)
        .getMergeRequestDiff(projectId, mergeRequestId).stream()
        .map(GitlabMergeRequestFileDiff::getNew_path)
        .collect(Collectors.toList());
  }

  @Override
  public List<MergeRequestDiscussion> getCommentsOfPr(ConnectionConfig connection, String mergeRequestId) {
    return getDiscussionsOfPr(connection, mergeRequestId)
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
  public String getFileOfBranch(ConnectionConfig connection, String branch, String filePath) {
    var projectId = connection.getConfigs().get("projects");
    return new GitlabRestClient(connection).getFileOfBranch(projectId, filePath, branch);
  }

  @Override
  public void addMergeRequestComment(ConnectionConfig connection, String mergeRequestId, CommentRequest comment) {
    var projectId = connection.getConfigs().get("projects");

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
  private List<GitlabDiscussionResponse> getDiscussionsOfPr(ConnectionConfig connection, String mergeRequestId) {
    var projectId = connection.getConfigs().get("projects");
    return new GitlabRestClient(connection).getDiscussions(projectId, mergeRequestId);
  }

  @SneakyThrows
  private List<GitlabMergeRequestResponse> getGitlabMergeRequests(ConnectionConfig connection) {
    return Arrays.stream(connection.getConfigs()
            .get("projects")
            .split(","))
        .map(x -> new GitlabRestClient(connection).getMergeRequests(x))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }
}
