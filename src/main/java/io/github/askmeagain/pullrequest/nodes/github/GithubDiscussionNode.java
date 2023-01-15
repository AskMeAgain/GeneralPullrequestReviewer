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
  private final MergeRequestDiscussion githubDiscussions;

  public GithubDiscussionNode(MergeRequestDiscussion mergeRequestDiscussion) {
    this.url = mergeRequestDiscussion.getUrl();
    this.githubDiscussions = mergeRequestDiscussion;
  }

  @Override
  public String toString() {
    return "Discussion: " + githubDiscussions.getDiscussionId();
  }

  public void refresh() {
    super.refresh();
    onCreation();
  }

  @Override
  public void onCreation() {
    removeAllChildren();
    githubDiscussions.getReviewComments()
        .stream()
        .map(ReviewComment::toString)
        .map(DefaultMutableTreeNode::new)
        .forEach(this::add);
  }
}
