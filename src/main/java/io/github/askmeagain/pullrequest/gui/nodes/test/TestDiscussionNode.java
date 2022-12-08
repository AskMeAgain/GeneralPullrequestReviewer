package io.github.askmeagain.pullrequest.gui.nodes.test;

import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;

@RequiredArgsConstructor
public class TestDiscussionNode extends BaseTreeNode {

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
