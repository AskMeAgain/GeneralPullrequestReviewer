package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.gui.nodes.FileNodes;
import io.github.askmeagain.pullrequest.gui.nodes.MergeRequestNode;
import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

@Service
public final class PluginManagementService {

  @Getter(lazy = true)
  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  private final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
  @Getter
  private final Tree tree = new Tree(rootNode);

  public void refreshList() {
    System.out.println("Refresh list!");

    rootNode.removeAllChildren();

    getDataRequestService().getMergeRequests().forEach(pr -> {
      var prNode = new DefaultMutableTreeNode(new MergeRequestNode(pr.getName(), pr.getId(), tree, getActiveProject(), pr.getSourceBranch(), pr.getTargetBranch()));
      rootNode.add(prNode);
      //hacky node
      prNode.add(new DefaultMutableTreeNode(new FileNodes("dummyfile.txt", getActiveProject(), null, null, null)));
    });

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
