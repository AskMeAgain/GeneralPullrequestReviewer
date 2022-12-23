package io.github.askmeagain.pullrequest.nodes.interfaces;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;

public interface ConnectionMarker extends NodeBehaviour {

  ConnectionConfig getConnection();
}
