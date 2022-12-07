package io.github.askmeagain.pullrequest.services.vcs;

import io.github.askmeagain.pullrequest.dto.application.CommentRequest;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;

import java.util.List;

public interface VcsService {

  List<MergeRequest> getMergeRequests(String connectionName);

  void addCommentToThread(String connectionName, String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request);

  List<String> getFilesOfPr(String connectionName, String mergeRequestId);

  List<MergeRequestDiscussion> getCommentsOfPr(String connectionName, String mergeRequestId);

  String getFileOfBranch(String connectionName, String branch, String filePath);

  void addMergeRequestComment(String connectionName, String mergeRequestId, CommentRequest comment);
}
