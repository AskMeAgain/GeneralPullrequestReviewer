package io.github.askmeagain.pullrequest.gui.nodes.interfaces;

public interface NodeBehaviour {

  void refresh();

  void onCreation();

  void beforeExpanded();

  void onClick();
}
