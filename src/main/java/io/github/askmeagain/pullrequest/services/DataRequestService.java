package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.github.GithubService;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.Getter;

import java.util.Map;

@Service
public final class DataRequestService {

  @Getter
  private final Map<VcsImplementation, VcsService> mapVcsImplementation = Map.of(
      VcsImplementation.GITLAB, GitlabService.getInstance(),
      VcsImplementation.GITHUB, GithubService.getInstance()
  );

  public static DataRequestService getInstance() {
    return ApplicationManager.getApplication().getService(DataRequestService.class);
  }
}
