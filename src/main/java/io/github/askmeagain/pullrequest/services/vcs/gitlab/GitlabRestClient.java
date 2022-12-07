package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.dto.gitlab.comment.GitlabMergeRequestCommentRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.GitlabDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.versions.MergeRequestVersions;
import io.github.askmeagain.pullrequest.services.StateService;
import io.github.askmeagain.pullrequest.settings.ConnectionConfig;
import lombok.Getter;

import java.util.Base64;
import java.util.List;


public class GitlabRestClient {

  private final GitlabApi gitlabApi;
  private final ConnectionConfig connectionConfig;

  public GitlabRestClient(ConnectionConfig config) {
    this.connectionConfig = config;
    gitlabApi = Feign.builder()
        .client(new OkHttpClient())
        .encoder(new JacksonEncoder())
        .decoder(new JacksonDecoder())
        .target(GitlabApi.class, connectionConfig.getConfigs().get("gitlabUrl"));
  }

  public List<GitlabMergeRequestResponse> getMergeRequests(String projectId) {
    return gitlabApi.getMergeRequests(projectId, getToken());
  }

  private String getToken() {
    return connectionConfig.getConfigs().get("token");
  }

  public String getFileOfBranch(String projectId, String filePath, String branch) {
    var encodedFilePath = filePath.replace(".", "%2E");

    var base64 = gitlabApi.getFileOfBranch(projectId, encodedFilePath, getToken(), branch).get("content");

    return new String(Base64.getDecoder().decode(base64));
  }

  public List<GitlabMergeRequestFileDiff> getMergeRequestDiff(String projectId, String mergeRequestId) {
    return gitlabApi.getMergerequestDiff(projectId, mergeRequestId, getToken());
  }

  public List<GitlabDiscussionResponse> getDiscussions(String project, String mergeRequestId) {
    return gitlabApi.getDiscussions(project, mergeRequestId, getToken());
  }

  public void addMergeRequestComment(String projectId, String mergeRequestId, GitlabMergeRequestCommentRequest request) {
    gitlabApi.addMergeRequestComment(request, projectId, mergeRequestId, getToken());
  }

  public List<MergeRequestVersions> getDiffVersion(String projectId, String mergeRequestId) {
    return gitlabApi.getDiffVersion(projectId, mergeRequestId, getToken());
  }

  public void addCommentToThread(
      String projectId,
      String mergeRequestId,
      String discussionId,
      GitlabAddCommentToDiscussionRequest request
  ) {
    gitlabApi.addCommentToThread(request, projectId, mergeRequestId, discussionId, getToken());
  }
}
