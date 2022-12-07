package io.github.askmeagain.pullrequest.gui;

import io.github.askmeagain.pullrequest.gui.nodes.gitlab.GitlabMergeRequestNode;
import io.github.askmeagain.pullrequest.gui.nodes.gitlab.NodeBehaviour;
import lombok.RequiredArgsConstructor;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;

@RequiredArgsConstructor
public class PluginTreeExpansionListener implements TreeWillExpandListener {

  @Override
  public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) {
    var lastNode = (DefaultMutableTreeNode) treeExpansionEvent.getPath().getLastPathComponent();

    if (lastNode instanceof NodeBehaviour) {
      var node = (NodeBehaviour) lastNode;
      node.onExpanded();
    }
  }

  @Override
  public void treeWillCollapse(TreeExpansionEvent treeExpansionEvent) {
    //do nothing
  }
}
