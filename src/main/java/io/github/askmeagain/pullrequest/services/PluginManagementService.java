package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.gui.nodes.ConnectionNode;
import io.github.askmeagain.pullrequest.gui.nodes.FileNodes;
import io.github.askmeagain.pullrequest.gui.nodes.MergeRequestNode;
import io.github.askmeagain.pullrequest.gui.nodes.ProjectNode;
import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

@Service
public final class PluginManagementService {

  private final DataRequestService dataRequestService = DataRequestService.getInstance();
  private final PullrequestPluginState state = StateService.getInstance().getState();

  private final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
  @Getter
  private final Tree tree = new Tree(rootNode);

  public void refreshList() {
    System.out.println("Refresh list!");

    rootNode.removeAllChildren();

    for (var connection : state.getConnectionConfigs()) {
      var connectionNode = new DefaultMutableTreeNode(new ConnectionNode(connection));
      rootNode.add(connectionNode);

      for (var project : connection.getConfigs().get("projects").split(",")) {
        var projectNode = new DefaultMutableTreeNode(new ProjectNode(project));
        connectionNode.add(projectNode);

        for (var mergeRequest : dataRequestService.getMergeRequests(connection.getName())) {
          var mergeRequestNode = new DefaultMutableTreeNode(new MergeRequestNode(
              mergeRequest.getName(),
              mergeRequest.getId(),
              tree,
              getActiveProject(),
              mergeRequest.getSourceBranch(),
              mergeRequest.getTargetBranch(),
              connection.getName()
          ));
          projectNode.add(mergeRequestNode);
          var userObject = new FileNodes(getActiveProject(), null, null, null, null, null);
          mergeRequestNode.add(new DefaultMutableTreeNode(userObject));
        }
      }
    }

    var model = (DefaultTreeModel) tree.getModel();
    model.reload();
  }

  public Project getActiveProject() {
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    for (Project project : projects) {
      Window window = WindowManager.getInstance().suggestParentWindow(project);
      if (window != null && window.isActive()) {
        return project;
      }
    }
    throw new RuntimeException("Could not find active project");
  }

  public static PluginManagementService getInstance() {
    return ApplicationManager.getApplication().getService(PluginManagementService.class);
  }

}
