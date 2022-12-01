package io.github.askmeagain.pullrequest.gui;

import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.gui.nodes.MergeRequestNode;
import lombok.RequiredArgsConstructor;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;

@RequiredArgsConstructor
public class MyTreeExpansionListener implements TreeWillExpandListener {

  @Override
  public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {
    var lastNode = (DefaultMutableTreeNode) treeExpansionEvent.getPath().getLastPathComponent();

    if(lastNode.getUserObject() instanceof MergeRequestNode){
      var mergeRequestNode = (MergeRequestNode) lastNode.getUserObject();
      mergeRequestNode.beforeOpening(lastNode);
    }
  }

  @Override
  public void treeWillCollapse(TreeExpansionEvent treeExpansionEvent) throws ExpandVetoException {

  }
}
