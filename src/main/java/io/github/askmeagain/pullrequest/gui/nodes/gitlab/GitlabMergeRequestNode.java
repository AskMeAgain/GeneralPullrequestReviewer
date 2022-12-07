package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.DataRequestService;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
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
  private final ConnectionConfig connection;

  private final GitlabService gitlabService = GitlabService.getInstance();

  @Override
  public String toString() {
    return String.format("%s: %s", mergeRequestId, display);
  }

  @Override
  public void onCreation() {
    add(new DefaultMutableTreeNode("hidden"));
  }

  @Override
  public void beforeExpanded() {
    removeAllChildren();

    gitlabService.getFilesOfPr(connection, mergeRequestId)
        .stream()
        .map(file -> new GitlabFileNode(project, sourceBranch, targetBranch, file, mergeRequestId, connection, tree))
        .forEach(this::add);
  }
}
