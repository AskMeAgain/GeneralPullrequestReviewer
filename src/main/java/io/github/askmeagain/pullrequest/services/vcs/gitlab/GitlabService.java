package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.GitlabDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.PersistenceManagementService;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public final class GitlabService implements VcsService {

  @Getter(lazy = true)
  private final PullrequestPluginState state = PersistenceManagementService.getInstance().getState();

  public static GitlabService getInstance() {
    return ApplicationManager.getApplication().getService(GitlabService.class);
  }

  @SneakyThrows
  public List<MergeRequest> getMergeRequests() {
    return getGitlabMergeRequests()
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
  public List<String> getFilesOfPr(String mergeRequestId) {
    var projectId = getState().getGitlabProjects().get(0);
    return GitlabRestClient.getInstance().getMergeRequestDiff(projectId, mergeRequestId).stream()
        .map(GitlabMergeRequestFileDiff::getNew_path)
        .collect(Collectors.toList());
  }

  @Override
  public List<ReviewComment> getCommentsOfPr(String mergeRequestId) {
    return getDiscussionsOfPr(mergeRequestId)
        .stream()
        .map(GitlabDiscussionResponse::getNotes)
        .flatMap(Collection::stream)
        .map(x -> {
          var isNotSource = x.getPosition().getOld_line() != 0;
          return ReviewComment.builder()
              .line(isNotSource ? x.getPosition().getOld_line(): x.getPosition().getNew_line())
              .sourceComment(!isNotSource)
              .text(x.getBody())
              .author(x.getAuthor().getName())
              .build();
        })
        .collect(Collectors.toList());
  }

  @Override
  public String getFileOfBranch(String branch, String filePath) {
    var projectId = getState().getGitlabProjects().get(0);
    return GitlabRestClient.getInstance().getFileOfBranch(projectId, filePath, branch);
  }

  @Override
  public void addMergeRequestComment(String mergeRequestId, String comment) {
    var projectId = getState().getGitlabProjects().get(0);
    GitlabRestClient.getInstance().addMergeRequestComment(projectId, mergeRequestId);
  }

  @SneakyThrows
  private List<GitlabDiscussionResponse> getDiscussionsOfPr(String mergeRequestId) {
    var projectId = getState().getGitlabProjects().get(0);
    return GitlabRestClient.getInstance().getDiscussions(projectId, mergeRequestId);
  }

  @SneakyThrows
  private List<GitlabMergeRequestResponse> getGitlabMergeRequests() {
    var projectId = getState().getGitlabProjects().get(0);
    return GitlabRestClient.getInstance().getMergeRequests(projectId);
  }

  @SneakyThrows
  private static String getReadString(String path) {
    return new String(PluginManagementService.class.getClassLoader().getResourceAsStream(path).readAllBytes());
  }
}
