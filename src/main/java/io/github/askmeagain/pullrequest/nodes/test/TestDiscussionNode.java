package io.github.askmeagain.pullrequest.nodes.test;

import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.DiscussionNodeMarker;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;

@RequiredArgsConstructor
public class TestDiscussionNode extends BaseTreeNode implements DiscussionNodeMarker {

  private final MergeRequestDiscussion discussion;

  @Override
  public String toString() {
    return "Discussion: " + discussion.getDiscussionId();
  }

  @Override
  public void onCreation() {
    discussion.getReviewComments()
        .stream()
        .map(ReviewComment::toString)
        .map(DefaultMutableTreeNode::new)
        .forEach(this::add);
  }
}
