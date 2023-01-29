package io.github.askmeagain.pullrequest.nodes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.listener.PluginTreeExpansionListener;
import io.github.askmeagain.pullrequest.nodes.interfaces.NodeBehaviour;
import io.github.askmeagain.pullrequest.services.ManagementService;
import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class BaseTreeNode extends DefaultMutableTreeNode implements NodeBehaviour {

  @Getter(lazy = true)
  private final DefaultTreeModel treeModel = (DefaultTreeModel) ManagementService.getInstance().getTree().getModel();
  @Getter(lazy = true)
  private final Tree tree = ManagementService.getInstance().getTree();
  @Getter(lazy = true)
  private final PluginTreeExpansionListener listener = PluginTreeExpansionListener.getInstance();

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

  protected void removeFakeNode() {
    if (getChildCount() != 1) {
      return;
    }

    if (getChildAt(0) instanceof FakeNode) {
      removeAllChildren();
    }
  }

  public boolean isCollapsed() {
    return getTree().isCollapsed(new TreePath(this.getPath()));
  }

  @Override
  public void onDoubleClick() {
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

  protected <T, X> List<X> getChilds(Function<T, X> func) {
    var list = new ArrayList<X>();
    for (int i = 0; i < getChildCount(); i++) {
      list.add(func.apply((T) getChildAt(i)));
    }
    return list;
  }

  protected <I, O extends BaseTreeNode> void addNewNodeFromLists(
      List<I> newValues,
      List<I> existingValues,
      Function<I, O> producer
  ) {
    for (var newValue : newValues) {
      if (!existingValues.contains(newValue)) {
        var newNode = producer.apply(newValue);
        getTreeModel().insertNodeInto(newNode, this, this.getChildCount());
        newNode.onCreation();
        getListener().doWithoutTriggers(() -> getTree().expandPath(new TreePath(this.getPath())));
      }
    }
  }

  protected <I, O extends NodeBehaviour> void removeOrRefreshNodes(
      List<I> newValues,
      List<O> oldValues,
      Function<O, I> transformer
  ) {
    for (var oldValue : oldValues) {
      if (!newValues.contains(transformer.apply(oldValue))) {
        getTreeModel().removeNodeFromParent(oldValue);
        getTreeModel().reload(this);
      }
    }

    //we have to do this afterwards, because of temp nodes
    for (var oldValue : oldValues) {
      if (newValues.contains(transformer.apply(oldValue))) {
        oldValue.refresh();
      }
    }
  }
}
