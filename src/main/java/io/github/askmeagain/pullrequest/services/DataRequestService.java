package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Service
public final class DataRequestService {

  private final Map<VcsImplementation, VcsService> mapVcsImplementation = Map.of(
      VcsImplementation.GITLAB, GitlabService.getInstance()
  );

  @Getter(lazy = true)
  private final PullrequestPluginState state = StateService.getInstance().getState();

  @Getter(lazy = true)
  private final VcsImplementation selectedImplementation = getState().getSelectedVcsImplementation();

  public List<MergeRequest> getMergeRequests() {
    return mapVcsImplementation.get(getSelectedImplementation()).getMergeRequests();
  }

  public List<String> getFilesOfPr(String mergeRequestId) {
    return mapVcsImplementation.get(getSelectedImplementation()).getFilesOfPr(mergeRequestId);
  }

  public List<MergeRequestDiscussion> getCommentsOfPr(String mergeRequestId) {
    return mapVcsImplementation.get(getSelectedImplementation()).getCommentsOfPr(mergeRequestId);
  }

  public void addMergeRequestComment(String mergeRequestId, CommentRequest comment) {
    mapVcsImplementation.get(getSelectedImplementation()).addMergeRequestComment(mergeRequestId, comment);
  }

  public void addCommentToThread(String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request) {
    mapVcsImplementation.get(getSelectedImplementation()).addCommentToThread(mergeRequestId, discussionId, request);
  }

  public String getFileOfBranch(String branch, String filePath) {
    return mapVcsImplementation.get(getSelectedImplementation()).getFileOfBranch(branch, filePath);
  }

  public static DataRequestService getInstance() {
    return ApplicationManager.getApplication().getService(DataRequestService.class);
  }
}
