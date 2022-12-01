package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import feign.Param;
import feign.RequestLine;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;

import java.util.List;

public interface GitlabApi {

  @RequestLine("GET /projects/{projectId}/merge_requests?state=opened&private_token={token}")
  List<GitlabMergeRequestResponse> getMergeRequests(
      @Param("projectId") String projectId,
      @Param("token") String token
  );

  @RequestLine("GET /projects/{projectId}/merge_requests/{merge_request_iid}/diffs?private_token={token}")
  List<GitlabMergeRequestFileDiff> getMergerequestDiff(
      @Param("projectId") String projectId,
      @Param("merge_request_iid") String mergeRequestId,
      @Param("token") String token
  );
}
