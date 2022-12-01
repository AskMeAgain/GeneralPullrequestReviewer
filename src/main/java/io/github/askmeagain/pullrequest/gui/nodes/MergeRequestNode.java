package io.github.askmeagain.pullrequest.gui.nodes;

import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

@RequiredArgsConstructor
public class MergeRequestNode {

  private final String display;
  private final String mergeRequestId;
  private final Tree tree;
  private final Project project;
  private final String sourceBranch;
  private final String targetBranch;

  @Getter(lazy = true)
  private final PluginManagementService pluginManagementService = PluginManagementService.getInstance();

  public void beforeOpening(DefaultMutableTreeNode lastNode) {
    lastNode.removeAllChildren();
    //get files now
    getPluginManagementService().getDataRequestService().getFilesOfPr(mergeRequestId).forEach(file -> {
      var newChild = new DefaultMutableTreeNode(new FileNodes(file, project, sourceBranch, targetBranch, file));
      lastNode.add(newChild);
    });
    var model = (DefaultTreeModel) tree.getModel();
    model.reload();
  }

  @Override
  public String toString() {
    return display;
  }
}
