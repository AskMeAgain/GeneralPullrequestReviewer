package io.github.askmeagain.pullrequest.services.vcs.github;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import io.github.askmeagain.pullrequest.dto.github.comment.GithubMergeRequestCommentRequest;
import io.github.askmeagain.pullrequest.dto.github.diffs.GithubDiffResponse;
import io.github.askmeagain.pullrequest.dto.github.discussionnote.GithubAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.dto.github.discussions.GithubDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.github.file.GithubFileContentResponse;
import io.github.askmeagain.pullrequest.dto.github.mergerequest.GithubMergeRequestResponse;

import java.util.List;

public interface GithubApi {

  @RequestLine("GET /{projectId}/pulls")
  List<GithubMergeRequestResponse> getMergeRequests(@Param("projectId") String projectId);

  @RequestLine("GET /{projectId}/pulls/{merge_request_iid}/files")
  List<GithubDiffResponse> getMergerequestDiff(
      @Param("projectId") String projectId,
      @Param("merge_request_iid") String mergeRequestId
  );

  @RequestLine(value = "GET /{projectId}/contents/{filePath}?ref={branch}", decodeSlash = false)
  GithubFileContentResponse getFileOfBranch(
      @Param("projectId") String projectId,
      @Param("filePath") String filePath,
      @Param("branch") String branch
  );

  @Headers("Accept: application/vnd.github.v3.diff")
  @RequestLine(value = "GET /{projectId}/pulls/{mergeRequestId}")
  Response getDiffHunksOfMergeRequest(
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId
  );

  @RequestLine("GET /{projectId}/pulls/{mergeRequestId}/comments")
  List<GithubDiscussionResponse> getDiscussions(
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId
  );

  @Headers("Content-Type: application/json")
  @RequestLine("POST /{projectId}/pulls/{mergeRequestId}/comments")
  void addMergeRequestComment(
      GithubMergeRequestCommentRequest body,
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId
  );

  //  @RequestLine("GET /projects/{projectId}/merge_requests/{mergeRequestId}/versions")
//  List<MergeRequestVersions> getDiffVersion(
//      @Param("projectId") String projectId,
//      @Param("mergeRequestId") String mergeRequestId
//  );
//
//  @RequestLine("GET /projects/{projectId}")
//  GitlabProjectResponse getProject(@Param("projectId") String projectId);

  @Headers("Content-Type: application/json")
  @RequestLine("POST /{projectId}/pulls/{mergeRequestId}/comments/{discId}/replies")
  void addCommentToThread(
      GithubAddCommentToDiscussionRequest request,
      @Param("projectId") String projectId,
      @Param("mergeRequestId") String mergeRequestId,
      @Param("discId") String discussionId
  );

//  @RequestLine("POST /projects/{projectId}/merge_requests/{mergeRequestId}/approve")
//  void approveMergeRequest(
//      @Param("projectId") String projectId,
//      @Param("mergeRequestId") String mergeRequestId
//  );
}
