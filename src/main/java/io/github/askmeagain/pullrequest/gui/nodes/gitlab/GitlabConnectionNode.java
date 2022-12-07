package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GitlabConnectionNode extends BaseTreeNode {

  private final ConnectionConfig connectionConfig;

  private final Tree tree;

  @Override
  public String toString() {
    return connectionConfig.getName();
  }

  @Override
  public void refresh() {
    this.removeAllChildren();
    onCreation();
  }

  @Override
  public void onCreation() {
    for (var project : connectionConfig.getConfigs().get("projects").split(",")) {
      var projectNode = new GitlabProjectNode(project, connectionConfig, tree);
      projectNode.onCreation();
      this.add(projectNode);
    }
  }
}
