package io.github.askmeagain.pullrequest.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.MergeRequest;
import io.github.askmeagain.pullrequest.dto.VcsImplementation;
import io.github.askmeagain.pullrequest.service.vcs.GitlabService;
import io.github.askmeagain.pullrequest.service.vcs.VcsService;

import java.util.List;
import java.util.Map;

@Service
public final class DataRequestService {

  //hardcoding right now
  private static final VcsImplementation VCS_IMPLEMENTATION = VcsImplementation.Gitlab;

  private final Map<VcsImplementation, VcsService> mapVcsImplementation;

  public DataRequestService() {
    mapVcsImplementation = Map.of(
        VcsImplementation.Gitlab, GitlabService.getInstance()
    );
  }

  public List<MergeRequest> getMergeRequests() {
    return mapVcsImplementation.get(VCS_IMPLEMENTATION).getMergeRequests();
  }


  public static DataRequestService getInstance() {
    return ApplicationManager.getApplication().getService(DataRequestService.class);
  }
}
