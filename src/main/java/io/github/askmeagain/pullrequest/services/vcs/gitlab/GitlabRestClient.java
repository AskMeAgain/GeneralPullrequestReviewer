package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.GitlabDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.services.PersistenceManagementService;
import lombok.Getter;

import java.util.Base64;
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

  public String getFileOfBranch(String projectId, String filePath, String branch) {
    var encodedFilePath = filePath.replace(".", "%2E");

    var base64 = gitlabApi.getFileOfBranch(projectId, encodedFilePath, getState().getGitlabToken(), branch).get("content");

    return new String(Base64.getDecoder().decode(base64));
  }

  public List<GitlabMergeRequestFileDiff> getMergeRequestDiff(String projectId, String mergeRequestId) {
    return gitlabApi.getMergerequestDiff(projectId, mergeRequestId, getState().getGitlabToken());
  }

  public List<GitlabDiscussionResponse> getDiscussions(String project, String mergeRequestId){
    return gitlabApi.getDiscussions(project, mergeRequestId, getState().getGitlabToken());
  }

  public void addMergeRequestComment(String projectId, String mergeRequestId) {
    gitlabApi.addMergeRequestComment(projectId, mergeRequestId, getState().getGitlabToken());
  }
}
