package io.github.askmeagain.pullrequest.nodes.gitlab;

import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.DiscussionNodeMarker;
import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;

public class GitlabDiscussionNode extends BaseTreeNode implements DiscussionNodeMarker {

  @Getter
  private final String url;
  @Getter
  private final boolean resolved;
  @Getter
  private final MergeRequestDiscussion discussion;

  public GitlabDiscussionNode(MergeRequestDiscussion discussion) {
    this.discussion = discussion;
    this.url = discussion.getUrl();
    this.resolved = discussion.isResolved();
  }

  @Override
  public String toString() {
    return resolved + "Discussion: " + discussion.getDiscussionId();
  }

  public void refresh() {
    super.refresh();
    onCreation();
  }

  @Override
  public void onCreation() {
    removeAllChildren();
    discussion.getReviewComments()
        .stream()
        .map(ReviewComment::toString)
        .map(DefaultMutableTreeNode::new)
        .forEach(this::add);
  }
}
