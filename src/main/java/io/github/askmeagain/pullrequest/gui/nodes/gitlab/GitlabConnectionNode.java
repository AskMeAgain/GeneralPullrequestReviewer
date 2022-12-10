package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.ConnectionMarker;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Arrays;

@RequiredArgsConstructor
public class GitlabConnectionNode extends BaseTreeNode implements ConnectionMarker {

  private final ConnectionConfig connectionConfig;

  private final GitlabService gitlabService = GitlabService.getInstance();

  @Override
  public String toString() {
    return connectionConfig.getName();
  }

  @Override
  public void onCreation() {
    add(new DefaultMutableTreeNode("hidden"));
  }

  @Override
  public void beforeExpanded() {
    removeAllChildren();

    Arrays.stream(connectionConfig.getConfigs()
            .get("projects")
            .split(","))
        .map(project -> new GitlabProjectNode(
            project,
            connectionConfig,
            gitlabService.getProject(connectionConfig, project).getName()
        ))
        .peek(GitlabProjectNode::onCreation)
        .forEach(this::add);
  }

  @Override
  public ConnectionConfig getConnection() {
    return connectionConfig;
  }
}
