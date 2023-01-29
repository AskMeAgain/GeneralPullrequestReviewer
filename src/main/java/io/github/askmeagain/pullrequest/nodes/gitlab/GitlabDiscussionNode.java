package io.github.askmeagain.pullrequest.nodes.gitlab;

import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.DiscussionNodeMarker;
import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;

public class GitlabDiscussionNode extends BaseTreeNode implements DiscussionNodeMarker {

  @Getter
  private String url;
  @Getter
  private boolean resolved;
  @Getter
  private MergeRequestDiscussion discussion;

  public GitlabDiscussionNode(MergeRequestDiscussion discussion) {
    this.discussion = discussion;
    this.url = discussion.getUrl();
    this.resolved = discussion.getResolved();
  }

  @Override
  public String toString() {
    return "Discussion: " + discussion.getDiscussionId();
  }

  @Override
  public void refresh(Object discussion) {
    super.refresh(discussion);

    var castedDiscussion = (MergeRequestDiscussion) discussion;

    this.discussion = castedDiscussion;
    this.url = castedDiscussion.getUrl();
    this.resolved = castedDiscussion.getResolved();
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
