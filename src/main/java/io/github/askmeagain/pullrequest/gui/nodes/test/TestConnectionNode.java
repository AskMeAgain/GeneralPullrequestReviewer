package io.github.askmeagain.pullrequest.gui.nodes.test;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.ConnectionMarker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestConnectionNode extends BaseTreeNode implements ConnectionMarker {

  @Getter
  private final ConnectionConfig connection;

  @Override
  public String toString() {
    return connection.getName();
  }

  @Override
  public void onCreation() {
    add(new TestProjectNode("CoolProject"));
  }
}
