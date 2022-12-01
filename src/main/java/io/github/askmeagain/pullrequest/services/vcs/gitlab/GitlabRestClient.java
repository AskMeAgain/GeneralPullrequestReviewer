package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.settings.PersistenceManagementService;
import lombok.Getter;

import java.util.List;

@Service
public final class GitlabRestClient {

  private final GitlabApi gitlabApi;

  public static GitlabRestClient getInstance() {
    return ApplicationManager.getApplication().getService(GitlabRestClient.class);
  }

  @Getter(lazy = true)
  private final PullrequestPluginState state = PersistenceManagementService.getInstance().getState();

  public GitlabRestClient() {
    gitlabApi = Feign.builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder())
        .decoder(new JacksonDecoder())
        .target(GitlabApi.class, getState().getGitlabUrl());
  }

  public List<GitlabMergeRequestResponse> getMergeRequests(String projectId) {
    return gitlabApi.getMergeRequests(projectId, getState().getGitlabToken());
  }

  public List<GitlabMergeRequestFileDiff> getMergeRequestDiff(String projectId, String mergeRequestId) {
    return gitlabApi.getMergerequestDiff(projectId, mergeRequestId, getState().getGitlabToken());
  }

}
