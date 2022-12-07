package io.github.askmeagain.pullrequest.gui.nodes;

import io.github.askmeagain.pullrequest.settings.ConnectionConfig;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ConnectionNode {

  private final ConnectionConfig connectionConfig;

  @Override
  public String toString(){
    return connectionConfig.getName();
  }
}
