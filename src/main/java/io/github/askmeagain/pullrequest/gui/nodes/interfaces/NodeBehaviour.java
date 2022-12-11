package io.github.askmeagain.pullrequest.gui.nodes.interfaces;

import javax.swing.tree.MutableTreeNode;

public interface NodeBehaviour extends MutableTreeNode {

  void refresh();

  void onCreation();

  void beforeExpanded();

  void onClick();
}
