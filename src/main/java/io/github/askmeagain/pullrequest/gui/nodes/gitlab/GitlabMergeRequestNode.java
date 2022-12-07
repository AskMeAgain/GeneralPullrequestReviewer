package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.DataRequestService;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

@RequiredArgsConstructor
public class GitlabMergeRequestNode extends BaseTreeNode {

  private final String display;
  private final String mergeRequestId;
  private final Tree tree;
  private final Project project;
  private final String sourceBranch;
  private final String targetBranch;
  private final String connectionName;

  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  @Override
  public String toString() {
    return "MergeRequest: " + display;
  }

  @Override
  public void onCreation() {
    var userObject = new GitlabFileNode(getActiveProject(), null, null, null, null, null, null);
    this.add(new DefaultMutableTreeNode(userObject));
  }

  @Override
  public void onExpanded() {
    this.removeAllChildren();
    //get files now
    dataRequestService.getFilesOfPr(connectionName, mergeRequestId)
        .forEach(file -> {
          var newChild = new GitlabFileNode(project, sourceBranch, targetBranch, file, mergeRequestId, connectionName, tree);
          this.add(newChild);
        });
    var model = (DefaultTreeModel) tree.getModel();
    model.reload();
  }
}
