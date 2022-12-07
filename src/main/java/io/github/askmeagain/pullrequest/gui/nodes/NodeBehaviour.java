package io.github.askmeagain.pullrequest.gui.nodes;

public interface NodeBehaviour {

  void refresh();

  void onCreation();

  void onExpanded();

  void onClick();
}
