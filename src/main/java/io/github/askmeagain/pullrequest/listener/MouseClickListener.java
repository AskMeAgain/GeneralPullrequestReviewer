package io.github.askmeagain.pullrequest.listener;

import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.NodeBehaviour;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@RequiredArgsConstructor
public class MouseClickListener extends MouseAdapter {
  private final Tree tree;

  public void mouseClicked(MouseEvent me) {
    TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
    if (tp != null) {
      var lastPathComponent = (DefaultMutableTreeNode) tp.getLastPathComponent();
      if (lastPathComponent instanceof NodeBehaviour) {
        var runnable = (NodeBehaviour) lastPathComponent;
        runnable.onClick();
      }
    }
  }
}
