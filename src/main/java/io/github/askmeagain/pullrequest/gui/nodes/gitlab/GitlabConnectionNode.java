package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.services.DataRequestService;
import io.github.askmeagain.pullrequest.settings.ConnectionConfig;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

@RequiredArgsConstructor
public class GitlabConnectionNode extends DefaultMutableTreeNode implements NodeBehaviour {

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

  @Override
  public void onExpanded() {

  }
}
