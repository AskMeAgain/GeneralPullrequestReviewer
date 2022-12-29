package io.github.askmeagain.pullrequest.services.vcs;

import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;

import java.util.List;

public interface VcsService {

  List<MergeRequest> getMergeRequests(String projectId, ConnectionConfig connectionName);

  void addCommentToThread(String projectId, ConnectionConfig connectionName, String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request);

  List<String> getFilesOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId);

  List<MergeRequestDiscussion> getCommentsOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId, String file);

  FileResponse getFileOfBranch(String projectId, ConnectionConfig connectionName, String branch, String filePath);

  void addMergeRequestComment(String projectId, ConnectionConfig connectionName, String mergeRequestId, CommentRequest comment);

  void approveMergeRequest(String projectId, ConnectionConfig connection, String mergeRequestId);
}
