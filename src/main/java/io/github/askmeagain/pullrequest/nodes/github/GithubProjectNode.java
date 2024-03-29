package io.github.askmeagain.pullrequest.nodes.github;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.dto.application.ProjectResponse;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.FakeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.ProjectMarker;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.github.GithubService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GithubProjectNode extends BaseTreeNode implements ProjectMarker {
  @Getter
  private final String url;
  private final ConnectionConfig connectionConfig;
  @Getter
  private final String projectName;
  private final VcsService githubService = GithubService.getInstance();

  public GithubProjectNode(ConnectionConfig connectionConfig, ProjectResponse projectResponse) {
    this.url = projectResponse.getUrl();
    this.projectName = projectResponse.getName();
    this.connectionConfig = connectionConfig;
  }

  @Override
  public String toString() {
    return String.format("%s", projectName);
  }

  @Override
  public void refresh(Object obj) {
    if (isCollapsed()) {
      return;
    }

    beforeExpanded();
  }

  @Override
  public void onCreation() {
    add(new FakeNode());
  }

  @Override
  public void beforeExpanded() {
    removeFakeNode();

    var mergeRequests = githubService.getMergeRequests(projectName, connectionConfig);

    var mergeRequestIds = mergeRequests.stream()
        .map(MergeRequest::getId)
        .collect(Collectors.toList());

    removeOrRefreshNodes(
        mergeRequestIds,
        this.getChilds(Function.identity()),
        GithubMergeRequestNode::getMergeRequestId
    );

    addNewNodeFromLists(mergeRequests, this.getChilds(GithubMergeRequestNode::getMergeRequest),
        mr -> new GithubMergeRequestNode(mr, connectionConfig, projectName));
  }
}
