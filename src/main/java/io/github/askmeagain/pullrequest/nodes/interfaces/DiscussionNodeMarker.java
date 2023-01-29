package io.github.askmeagain.pullrequest.nodes.interfaces;

import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;

public interface DiscussionNodeMarker extends OpenUrlMarker {
  boolean isResolved();

  MergeRequestDiscussion getDiscussion();
}
