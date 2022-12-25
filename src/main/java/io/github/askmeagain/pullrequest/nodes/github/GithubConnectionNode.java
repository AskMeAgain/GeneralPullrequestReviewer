package io.github.askmeagain.pullrequest.nodes.github;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.FakeNode;
import io.github.askmeagain.pullrequest.nodes.gitlab.GitlabProjectNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.ConnectionMarker;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class GithubConnectionNode extends BaseTreeNode implements ConnectionMarker {

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
