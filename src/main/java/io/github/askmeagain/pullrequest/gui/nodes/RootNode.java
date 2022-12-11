package io.github.askmeagain.pullrequest.gui.nodes;

import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.gui.nodes.gitlab.GitlabConnectionNode;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.ConnectionMarker;
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
        .map(GitlabConnectionNode::new)
        .peek(GitlabConnectionNode::onCreation)
        .forEach(this::add);
  }
}
