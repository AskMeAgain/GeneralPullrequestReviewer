package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

public interface NodeBehaviour {

  void refresh();

  void onCreation();

  void onExpanded();

  void onClick();
}
