package io.github.askmeagain.pullrequest.nodes.interfaces;

public interface MergeRequestMarker {

  String getTargetBranchName();

  Boolean getCanBeMerged();
}
