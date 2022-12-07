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
import io.github.askmeagain.pullrequest.services.StateService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import lombok.Getter;
import lombok.SneakyThrows;

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
  public List<MergeRequest> getMergeRequests(String connectionName) {
    return getGitlabMergeRequests(connectionName)
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
  public void addCommentToThread(String connectionName, String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request) {
    var projectId = getState().getMap().get(connectionName).getConfigs().get("projectId");

    new GitlabRestClient(getState().getMap().get(connectionName)).addCommentToThread(projectId, mergeRequestId, discussionId, request);
  }

  @Override
  public List<String> getFilesOfPr(String connectionName, String mergeRequestId) {
    var projectId = getState().getMap().get(connectionName).getConfigs().get("projectId");
    return new GitlabRestClient(getState().getMap().get(connectionName)).getMergeRequestDiff(projectId, mergeRequestId).stream()
        .map(GitlabMergeRequestFileDiff::getNew_path)
        .collect(Collectors.toList());
  }

  @Override
  public List<MergeRequestDiscussion> getCommentsOfPr(String connectionName, String mergeRequestId) {
    return getDiscussionsOfPr(connectionName, mergeRequestId)
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
  public String getFileOfBranch(String connectionName, String branch, String filePath) {
    var projectId = getState().getMap().get(connectionName).getConfigs().get("projectId");
    return new GitlabRestClient(getState().getMap().get(connectionName)).getFileOfBranch(projectId, filePath, branch);
  }

  @Override
  public void addMergeRequestComment(String connectionName, String mergeRequestId, CommentRequest comment) {
    var projectId = getState().getMap().get(connectionName).getConfigs().get("projectId");

    var diffVersion = new GitlabRestClient(getState().getMap().get(connectionName)).getDiffVersion(projectId, mergeRequestId).get(0);

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

    new GitlabRestClient(getState().getMap().get(connectionName)).addMergeRequestComment(projectId, mergeRequestId, request);
  }

  @SneakyThrows
  private List<GitlabDiscussionResponse> getDiscussionsOfPr(String connectionName, String mergeRequestId) {
    var projectId = getState().getMap().get(connectionName).getConfigs().get("projectId");
    return new GitlabRestClient(getState().getMap().get(connectionName)).getDiscussions(projectId, mergeRequestId);
  }

  @SneakyThrows
  private List<GitlabMergeRequestResponse> getGitlabMergeRequests(String connectionName) {
    var projectId = getState().getMap().get(connectionName).getConfigs().get("projectId");
    return new GitlabRestClient(getState().getMap().get(connectionName)).getMergeRequests(projectId);
  }
}
