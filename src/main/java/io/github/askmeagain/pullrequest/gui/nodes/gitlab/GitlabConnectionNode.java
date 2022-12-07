package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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
    Arrays.stream(connectionConfig.getConfigs()
            .get("projects")
            .split(","))
        .map(project -> new GitlabProjectNode(project, connectionConfig, tree))
        .peek(GitlabProjectNode::onCreation)
        .forEach(this::add);
  }
}
