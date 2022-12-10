package io.github.askmeagain.pullrequest.gui.nodes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.treeStructure.Tree;
import groovy.util.NodeList;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.NodeBehaviour;
import io.github.askmeagain.pullrequest.services.ManagementService;
import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class BaseTreeNode extends DefaultMutableTreeNode implements NodeBehaviour {

  @Getter(lazy = true)
  private final DefaultTreeModel treeModel = (DefaultTreeModel) ManagementService.getInstance().getTree().getModel();
  @Getter(lazy = true)
  private final Tree tree = ManagementService.getInstance().getTree();

  @Override
  public void refresh() {
    for (int i = 0; i < getChildCount(); i++) {
      var child = getChildAt(i);
      if (child instanceof NodeBehaviour) {
        var casted = (NodeBehaviour) child;
        casted.refresh();
      }
    }
  }

  @Override
  public void onCreation() {

  }

  @Override
  public void beforeExpanded() {

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

  protected <T,X> List<X> getChilds(Function<T, X> func) {
    var list = new ArrayList<X>();
    for (int i = 0; i < getChildCount(); i++) {
      list.add(func.apply((T) getChildAt(i)));
    }
    return list;
  }
}
