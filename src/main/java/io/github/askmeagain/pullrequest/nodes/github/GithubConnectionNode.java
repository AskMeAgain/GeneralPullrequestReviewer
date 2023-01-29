package io.github.askmeagain.pullrequest.nodes.github;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.FakeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.ConnectionMarker;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.github.GithubService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class GithubConnectionNode extends BaseTreeNode implements ConnectionMarker {

  @Getter
  private final ConnectionConfig connection;

  @Override
  public String toString() {
    return connection.getName();
  }

  private final VcsService githubService = GithubService.getInstance();

  @Override
  public void onCreation() {
    add(new FakeNode());
  }

  @Override
  public void refresh(Object obj) {
    if (isCollapsed()) {
      return;
    }
    super.refresh();
  }

  @Override
  public void beforeExpanded() {
    removeFakeNode();

    var projectList = List.of(connection.getConfigs()
        .get("repoName")
        .split(","));

    removeOrRefreshNodes(projectList, this.getChilds(Function.identity()), GithubProjectNode::getProjectName);
    addNewNodeFromLists(projectList, getChilds(GithubProjectNode::getProjectName), projectId -> new GithubProjectNode(
        connection,
        githubService.getProject(connection, projectId)
    ));
  }
}
