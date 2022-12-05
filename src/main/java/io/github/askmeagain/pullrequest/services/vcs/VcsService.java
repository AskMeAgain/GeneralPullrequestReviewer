package io.github.askmeagain.pullrequest.services.vcs;

import io.github.askmeagain.pullrequest.dto.application.CommentRequest;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;

import java.util.List;

public interface VcsService {

  List<MergeRequest> getMergeRequests();

  void addCommentToThread(String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request);

  List<String> getFilesOfPr(String mergeRequestId);

  List<ReviewComment> getCommentsOfPr(String mergeRequestId);

  String getFileOfBranch(String branch, String filePath);

  void addMergeRequestComment(String mergeRequestId, CommentRequest comment);
}
