package io.github.askmeagain.pullrequest.nodes.gitlab;

import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.DiscussionNodeMarker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;

@RequiredArgsConstructor
public class GitlabDiscussionNode extends BaseTreeNode implements DiscussionNodeMarker {

  @Getter
  private final String url;
  private final MergeRequestDiscussion gitlabDiscussion;

  public GitlabDiscussionNode(MergeRequestDiscussion gitlabDiscussion) {
    this.gitlabDiscussion = gitlabDiscussion;
    this.url = gitlabDiscussion.getUrl();
  }

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
    removeAllChildren();
    gitlabDiscussion.getReviewComments()
        .stream()
        .map(ReviewComment::toString)
        .map(DefaultMutableTreeNode::new)
        .forEach(this::add);
  }
}
