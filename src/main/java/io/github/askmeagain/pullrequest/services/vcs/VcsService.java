package io.github.askmeagain.pullrequest.services.vcs;

import io.github.askmeagain.pullrequest.dto.application.*;

import java.util.List;

public interface VcsService {

  List<MergeRequest> getMergeRequests(String projectId, ConnectionConfig connectionName);

  void addCommentToThread(String projectId, ConnectionConfig connectionName, String mergeRequestId, String discussionId, String body);

  void editComment(ConnectionConfig connectionName, String projectId, String mergeRequestId, String discussionId, String note_id, String body);

  void deleteComment(ConnectionConfig connectionName, String projectId, String mergeRequestId, String discussionId, String note_id);

  List<String> getFilesOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId);

  List<MergeRequestDiscussion> getCommentsOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId, String file);

  FileResponse getFileOfBranch(String projectId, ConnectionConfig connectionName, String branch, String filePath);

  void addMergeRequestComment(String projectId, ConnectionConfig connectionName, String mergeRequestId, CommentRequest comment);

  void approveMergeRequest(String projectId, ConnectionConfig connection, String mergeRequestId);

  ProjectResponse getProject(ConnectionConfig connection, String projectId);

  String getDiffHunk(String projectId, ConnectionConfig connection, String mergeRequestId);
}
