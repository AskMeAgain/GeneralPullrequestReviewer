package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.gui.nodes.FakeNode;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.ConnectionMarker;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import kotlin.OptIn;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class GitlabConnectionNode extends BaseTreeNode implements ConnectionMarker {

  private final ConnectionConfig connectionConfig;

  private final GitlabService gitlabService = GitlabService.getInstance();

  @Override
  public String toString() {
    return connectionConfig.getName();
  }

  @Override
  public void onCreation() {
    add(new FakeNode());
  }

  @Override
  public void refresh() {
    if (isCollapsed()) {
      return;
    }
    super.refresh();
  }

  @Override
  public void beforeExpanded() {
    removeFakeNode();

    var projectList = List.of(connectionConfig.getConfigs()
        .get("projects")
        .split(","));

    removeOrRefreshNodes(projectList, this.getChilds(Function.identity()), GitlabProjectNode::getProjectId);
    addNewNodeFromLists(projectList, getChilds(GitlabProjectNode::getProjectId), project -> new GitlabProjectNode(
        project,
        connectionConfig,
        gitlabService.getProject(connectionConfig, project).getName()
    ));
  }

  @Override
  public ConnectionConfig getConnection() {
    return connectionConfig;
  }
}
