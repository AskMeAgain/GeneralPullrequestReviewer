package io.github.askmeagain.pullrequest.nodes.gitlab;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.FakeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.ConnectionMarker;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class GitlabConnectionNode extends BaseTreeNode implements ConnectionMarker {

  private final ConnectionConfig connectionConfig;

  private final VcsService gitlabService = GitlabService.getInstance();

  @Override
  public String toString() {
    return connectionConfig.getName();
  }

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

    var projectList = List.of(connectionConfig.getConfigs()
        .get("projects")
        .split(","));

    removeOrRefreshNodes(projectList, this.getChilds(Function.identity()), GitlabProjectNode::getProjectId);
    addNewNodeFromLists(projectList, getChilds(GitlabProjectNode::getProjectId), projectId -> new GitlabProjectNode(
        connectionConfig,
        gitlabService.getProject(connectionConfig, projectId)
    ));
  }

  @Override
  public ConnectionConfig getConnection() {
    return connectionConfig;
  }
}
