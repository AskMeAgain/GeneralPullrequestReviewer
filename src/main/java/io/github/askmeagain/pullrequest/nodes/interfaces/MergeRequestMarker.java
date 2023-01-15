package io.github.askmeagain.pullrequest.nodes.interfaces;

import java.util.List;

public interface MergeRequestMarker extends OpenUrlMarker {

  String getSourceBranch();

  Boolean getCanBeMerged();

  void approveMergeRequest();

  List<String> getReviewerUrls();
}
