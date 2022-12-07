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

  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  @Override
  public String toString(){
    return connectionConfig.getName();
  }

  @Override
  public void refresh() {
    this.removeAllChildren();
    onCreation();
  }

  @Override
  public void onCreation() {
    var activeProject = getActiveProject();
      for (var project : connectionConfig.getConfigs().get("projects").split(",")) {
        var projectNode = new DefaultMutableTreeNode(new ProjectNode(project));
        this.add(projectNode);

        for (var mergeRequest : dataRequestService.getMergeRequests(connectionConfig.getName())) {
          var mergeRequestNode = new DefaultMutableTreeNode(new MergeRequestNode(
              mergeRequest.getName(),
              mergeRequest.getId(),
              tree,
              activeProject,
              mergeRequest.getSourceBranch(),
              mergeRequest.getTargetBranch(),
              connectionConfig.getName()
          ));
          projectNode.add(mergeRequestNode);
          var userObject = new FileNodes(activeProject, null, null, null, null, null);
          mergeRequestNode.add(new DefaultMutableTreeNode(userObject));
        }
      }
  }

  @Override
  public void onExpanded() {

  }

  private Project getActiveProject() {
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    for (Project project : projects) {
      Window window = WindowManager.getInstance().suggestParentWindow(project);
      if (window != null && window.isActive()) {
        return project;
      }
    }
    throw new RuntimeException("Could not find active project");
  }
}
