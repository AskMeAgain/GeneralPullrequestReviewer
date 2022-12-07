package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GitlabProjectNode extends BaseTreeNode {
  private final String project;
  private final ConnectionConfig connectionConfig;

  private String projectName;

  private final GitlabService gitlabService = GitlabService.getInstance();
  private final Tree tree;


  @Override
  public String toString() {
    return "Project: " + projectName;
  }

  @Override
  public void refresh() {
    this.removeAllChildren();
    onCreation();
  }

  @Override
  public void onCreation() {
    var activeProject = getActiveProject();

    projectName = gitlabService.getProject(connectionConfig.getName(), project).getName();

    for (var mergeRequest : gitlabService.getMergeRequests(connectionConfig.getName())) {
      var mergeRequestNode = new GitlabMergeRequestNode(
          mergeRequest.getName(),
          mergeRequest.getId(),
          tree,
          activeProject,
          mergeRequest.getSourceBranch(),
          mergeRequest.getTargetBranch(),
          connectionConfig.getName()
      );
      mergeRequestNode.onCreation();
      this.add(mergeRequestNode);
    }
  }
}
