package io.github.askmeagain.pullrequest.gui.nodes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class BaseTreeNode extends DefaultMutableTreeNode implements NodeBehaviour {

  @Override
  public void refresh() {

  }

  @Override
  public void onCreation() {

  }

  @Override
  public void onExpanded() {

  }

  @Override
  public void onClick() {

  }

  protected Project getActiveProject() {
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
