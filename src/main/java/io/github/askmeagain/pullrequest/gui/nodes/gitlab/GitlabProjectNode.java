package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import io.github.askmeagain.pullrequest.settings.ConnectionConfig;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

@RequiredArgsConstructor
public class GitlabProjectNode extends DefaultMutableTreeNode implements NodeBehaviour {
  private final String project;
  private final ConnectionConfig connectionConfig;

  private String projectName;

  private final GitlabService gitlabService = GitlabService.getInstance();
  private final Tree tree;


  @Override
  public String toString() {
    return projectName;
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

  @Override
  public void onExpanded() {

  }

  @Override
  public void onClick() {

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
