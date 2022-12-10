package io.github.askmeagain.pullrequest.gui.nodes.interfaces;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;

import javax.swing.tree.MutableTreeNode;

public interface ConnectionMarker extends MutableTreeNode {

  ConnectionConfig getConnection();
}
