package io.github.askmeagain.pullrequest.nodes.interfaces;

import javax.swing.tree.MutableTreeNode;

public interface NodeBehaviour extends MutableTreeNode {

  void refresh(Object object);

  default void refresh() {
    refresh(null);
  }

  void onCreation();

  void beforeExpanded();

  void onClick();

  void onDoubleClick();

}
