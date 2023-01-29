package io.github.askmeagain.pullrequest.nodes.github;

import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.DiscussionNodeMarker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;

@RequiredArgsConstructor
public class GithubDiscussionNode extends BaseTreeNode implements DiscussionNodeMarker {
  @Getter
  private final String url;
  @Getter
  private final boolean resolved;
  @Getter
  private final MergeRequestDiscussion discussion;

  public GithubDiscussionNode(MergeRequestDiscussion discussion) {
    this.url = discussion.getUrl();
    this.discussion = discussion;
    this.resolved = discussion.isResolved();
  }

  @Override
  public String toString() {
    return "Discussion: " + discussion.getDiscussionId();
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
