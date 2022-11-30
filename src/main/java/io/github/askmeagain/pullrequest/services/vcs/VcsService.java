package io.github.askmeagain.pullrequest.services.vcs;

import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;

import java.util.List;

public interface VcsService {

  List<MergeRequest> getMergeRequests();

  List<String> getFilesOfPr();

  List<ReviewComment> getCommentsOfPr();
  String getFileOfBranch(String branch);

}
