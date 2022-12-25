package io.github.askmeagain.pullrequest.nodes.interfaces;

public interface MergeRequestMarker {

  String getSourceBranch();

  Boolean getCanBeMerged();

  void approveMergeRequest();
}
