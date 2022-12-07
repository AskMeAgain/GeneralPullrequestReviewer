package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;

@RequiredArgsConstructor
public class GitlabProjectNode extends BaseTreeNode {
  private final String projectId;
  private final ConnectionConfig connectionConfig;


  private final Tree tree;
  private final String projectName;
  private final GitlabService gitlabService = GitlabService.getInstance();

  @Override
  public String toString() {
    return String.format("%s (%s)", projectName, projectId);
  }

  @Override
  public void refresh() {
    this.removeAllChildren();
    onCreation();
  }

  @Override
  public void onCreation() {
    add(new DefaultMutableTreeNode("hidden"));
  }

  @Override
  public void beforeExpanded() {
    removeAllChildren();

    var activeProject = getActiveProject();

    gitlabService.getMergeRequests(connectionConfig)
        .stream()
        .map(mergeRequest -> new GitlabMergeRequestNode(
            mergeRequest.getName(),
            mergeRequest.getId(),
            tree,
            activeProject,
            mergeRequest.getSourceBranch(),
            mergeRequest.getTargetBranch(),
            connectionConfig
        ))
        .peek(GitlabMergeRequestNode::onCreation)
        .forEach(this::add);
  }
}
