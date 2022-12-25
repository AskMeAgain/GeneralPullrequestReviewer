package io.github.askmeagain.pullrequest.services.vcs.gitlab;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.github.askmeagain.pullrequest.dto.gitlab.comment.GitlabMergeRequestCommentRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.diffs.GitlabMergeRequestFileDiff;
import io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy.GitlabDiffsLegacyResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.GitlabDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.project.GitlabProjectResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.versions.MergeRequestVersions;

import java.util.List;
import java.util.Map;

public interface GitlabApi {

  @RequestLine("GET /projects/{projectId}/merge_requests?state=opened")
  List<GitlabMergeRequestResponse> getMergeRequests(@Param("projectId") String projectId);

  @RequestLine("GET /projects/{projectId}/merge_requests/{merge_request_iid}/diffs")
  List<GitlabMergeRequestFileDiff> getMergerequestDiff(
      @Param("projectId") String projectId,
      @Param("merge_request_iid") String mergeRequestId
  );

  @RequestLine("GET /projects/{projectId}/merge_requests/{merge_request_iid}/changes")
  GitlabDiffsLegacyResponse getMergerequestDiffLegacy(
      @Param("projectId") String projectId,
      @Param("merge_request_iid") String mergeRequestId
  );

  @RequestLine(value = "GET /projects/{projectId}/repository/files/{filePath}?ref={branch}", decodeSlash = false)
  Map<String, String> getFileOfBranch(
      @Param("projectId") String projectId,
      @Param("filePath") String filePath,
      @Param("branch") String branch
  );

  @RequestLine("GET /projects/{projectId}/merge_requests/{mergeRequestId}/discussions")
  List<GitlabDiscussionResponse> getDiscussions(
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId
  );

  @Headers("Content-Type: application/json")
  @RequestLine("POST /projects/{projectId}/merge_requests/{mergeRequestId}/discussions")
  void addMergeRequestComment(
      GitlabMergeRequestCommentRequest body,
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId
  );

  @RequestLine("GET /projects/{projectId}/merge_requests/{mergeRequestId}/versions")
  List<MergeRequestVersions> getDiffVersion(
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId
  );

  @RequestLine("GET /projects/{projectId}")
  GitlabProjectResponse getProject(@Param("projectId") String projectId);

  @Headers("Content-Type: application/json")
  @RequestLine("POST /projects/{projectId}/merge_requests/{mergeRequestId}/discussions/{discId}/notes")
  void addCommentToThread(
      GitlabAddCommentToDiscussionRequest request,
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId,
      @Param("discId") String discussionId
  );

  @RequestLine("POST /projects/{projectId}/merge_requests/{mergeRequestId}/approve")
  void approveMergeRequest(
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId
  );
}
