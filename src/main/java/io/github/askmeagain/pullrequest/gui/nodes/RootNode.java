package io.github.askmeagain.pullrequest.gui.nodes;

import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.gui.nodes.gitlab.GitlabConnectionNode;
import io.github.askmeagain.pullrequest.services.StateService;

public class RootNode extends BaseTreeNode {

  private final PullrequestPluginState state = StateService.getInstance().getState();

  public RootNode() {
    onCreation();
  }

  @Override
  public void refresh() {
    onCreation();
  }

  @Override
  public void onCreation() {
    removeAllChildren();
    state.getConnectionConfigs()
        .stream()
        .map(GitlabConnectionNode::new)
        .peek(GitlabConnectionNode::onCreation)
        .forEach(this::add);
  }
}
