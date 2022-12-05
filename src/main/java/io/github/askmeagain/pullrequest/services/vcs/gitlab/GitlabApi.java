package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.github.askmeagain.pullrequest.dto.gitlab.comment.GitlabMergeRequestCommentRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.GitlabDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.versions.MergeRequestVersions;

import java.util.List;
import java.util.Map;

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

  @RequestLine("GET /projects/{projectId}/repository/files/{filePath}?private_token={token}&ref={branch}")
  Map<String, String> getFileOfBranch(
      @Param("projectId") String projectId,
      @Param("filePath") String filePath,
      @Param("token") String token,
      @Param("branch") String branch
  );

  @RequestLine("GET /projects/{projectId}/merge_requests/{mergeRequestId}/discussions?private_token={token}")
  List<GitlabDiscussionResponse> getDiscussions(
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId,
      @Param("token") String token
  );

  @Headers("Content-Type: application/json")
  @RequestLine("POST /projects/{projectId}/merge_requests/{mergeRequestId}/discussions?private_token={token}")
  void addMergeRequestComment(
      GitlabMergeRequestCommentRequest body,
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId,
      @Param("token") String gitlabToken
  );

  @RequestLine("GET /projects/{projectId}/merge_requests/{mergeRequestId}/versions?private_token={token}")
  List<MergeRequestVersions> getDiffVersion(
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId,
      @Param("token") String gitlabToken);

  @Headers("Content-Type: application/json")
  @RequestLine("POST /projects/{projectId}/merge_requests/{mergeRequestId}/discussions/{discId}/notes?private_token={token}")
  void addCommentToThread(
      GitlabAddCommentToDiscussionRequest request,
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId,
      @Param("discId") String discussionId,
      @Param("token") String gitlabToken
  );
}
