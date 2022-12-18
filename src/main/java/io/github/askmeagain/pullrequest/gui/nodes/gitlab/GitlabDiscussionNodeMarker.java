package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.DiscussionNodeMarker;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GitlabDiscussionNodeMarker extends BaseTreeNode implements DiscussionNodeMarker {

  private final MergeRequestDiscussion gitlabDiscussion;

  @Override
  public String toString() {
    return "Discussion: " + gitlabDiscussion.getDiscussionId();
  }

  public void refresh() {
    super.refresh();
    onCreation();
  }

  @Override
  public void onCreation() {
//    removeAllChildren();
//    gitlabDiscussion.getReviewComments()
//        .stream()
//        .map(ReviewComment::toString)
//        .map(DefaultMutableTreeNode::new)
//        .forEach(this::addNode);
  }
}
