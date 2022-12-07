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

  private final PullrequestPluginState state = StateService.getInstance().getState();

  public List<String> getFilesOfPr(String connectionName, String mergeRequestId) {
    return mapVcsImplementation.get(getVcsImplementation(connectionName)).getFilesOfPr(connectionName, mergeRequestId);
  }

  public List<MergeRequestDiscussion> getCommentsOfPr(String connectionName, String mergeRequestId) {
    return mapVcsImplementation.get(getVcsImplementation(connectionName)).getCommentsOfPr(connectionName, mergeRequestId);
  }

  public void addMergeRequestComment(String connectionName, String mergeRequestId, CommentRequest comment) {
    mapVcsImplementation.get(getVcsImplementation(connectionName)).addMergeRequestComment(connectionName, mergeRequestId, comment);
  }

  public void addCommentToThread(String connectionName, String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request) {
    mapVcsImplementation.get(getVcsImplementation(connectionName)).addCommentToThread(connectionName, mergeRequestId, discussionId, request);
  }

  public String getFileOfBranch(String connectionName, String branch, String filePath) {
    return mapVcsImplementation.get(getVcsImplementation(connectionName)).getFileOfBranch(connectionName, branch, filePath);
  }

  public static DataRequestService getInstance() {
    return ApplicationManager.getApplication().getService(DataRequestService.class);
  }

  private VcsImplementation getVcsImplementation(String connectionName) {
    return state.getMap().get(connectionName).getVcsImplementation();
  }
}
