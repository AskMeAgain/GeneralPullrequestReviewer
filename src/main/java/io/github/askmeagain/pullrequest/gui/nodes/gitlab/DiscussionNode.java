package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;

import javax.swing.tree.DefaultMutableTreeNode;

public class DiscussionNode extends DefaultMutableTreeNode implements NodeBehaviour{

  private final MergeRequestDiscussion gitlabDiscussion;

  @Override
  public String toString(){
    return gitlabDiscussion.getDiscussionId();
  }

  public DiscussionNode(MergeRequestDiscussion gitlabDiscussion) {
    this.gitlabDiscussion = gitlabDiscussion;
  }

  @Override
  public void refresh() {

  }

  @Override
  public void onCreation() {
    for(var comment: gitlabDiscussion.getReviewComments()){
      this.add(new DefaultMutableTreeNode(comment.getText()));
    }
  }

  @Override
  public void onExpanded() {

  }

  @Override
  public void onClick() {

  }
}
