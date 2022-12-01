package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.settings.PersistenceManagementService;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Service
public final class DataRequestService {

  private final Map<VcsImplementation, VcsService> mapVcsImplementation = Map.of(
      VcsImplementation.GITLAB, GitlabService.getInstance()
  );

  @Getter(lazy = true)
  private final PullrequestPluginState state = PersistenceManagementService.getInstance().getState();

  @Getter(lazy = true)
  private final VcsImplementation selectedImplementation = getState().getSelectedVcsImplementation();

  public List<MergeRequest> getMergeRequests() {
    return mapVcsImplementation.get(getSelectedImplementation()).getMergeRequests();
  }

  public List<String> getFilesOfPr(String mergeRequestId) {
    return mapVcsImplementation.get(getSelectedImplementation()).getFilesOfPr(mergeRequestId);
  }

  public List<ReviewComment> getCommentsOfPr() {
    return mapVcsImplementation.get(getSelectedImplementation()).getCommentsOfPr();
  }

  public String getFileOfBranch(String branch, String filePath) {
    return mapVcsImplementation.get(getSelectedImplementation()).getFileOfBranch(branch, filePath);
  }

  public static DataRequestService getInstance() {
    return ApplicationManager.getApplication().getService(DataRequestService.class);
  }
}
