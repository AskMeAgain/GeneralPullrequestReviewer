package io.github.askmeagain.pullrequest.gui.nodes;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.gui.nodes.gitlab.GitlabConnectionNode;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.ConnectionMarker;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.NodeBehaviour;
import io.github.askmeagain.pullrequest.gui.nodes.test.TestConnectionNode;
import io.github.askmeagain.pullrequest.services.StateService;

import java.util.function.Function;

public class RootNode extends BaseTreeNode {

  private final PullrequestPluginState state = StateService.getInstance().getState();

  public RootNode() {
    onCreation();
  }

  @Override
  public void refresh() {
    removeOrRefreshNodes(
        state.getConnectionConfigs(),
        this.getChilds(Function.identity()),
        ConnectionMarker::getConnection);

    addNewNodeFromLists(
        state.getConnectionConfigs(),
        this.getChilds(ConnectionMarker::getConnection),
        GitlabConnectionNode::new
    );
  }

  @Override
  public void onCreation() {
    state.getConnectionConfigs()
        .stream()
        .map(this::resolveNode)
        .peek(NodeBehaviour::onCreation)
        .forEach(this::add);
  }

  private NodeBehaviour resolveNode(ConnectionConfig connectionConfig) {
    switch (connectionConfig.getVcsImplementation()) {
      case TEST:
        return new TestConnectionNode(connectionConfig);
      case GITLAB:
        return new GitlabConnectionNode(connectionConfig);
    }
    return null;
  }
}
