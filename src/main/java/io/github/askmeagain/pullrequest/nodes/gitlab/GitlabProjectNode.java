package io.github.askmeagain.pullrequest.nodes.gitlab;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.FakeNode;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GitlabProjectNode extends BaseTreeNode {
  @Getter
  private final String projectId;
  private final ConnectionConfig connectionConfig;
  private final String projectName;
  private final VcsService gitlabService = GitlabService.getInstance();

  @Override
  public String toString() {
    return String.format("%s (%s)", projectName, projectId);
  }

  @Override
  public void refresh() {
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

    var mergeRequests = gitlabService.getMergeRequests(projectId, connectionConfig);

    var mergeRequestIds = mergeRequests.stream()
        .map(MergeRequest::getId)
        .collect(Collectors.toList());

    removeOrRefreshNodes(
        mergeRequestIds,
        this.getChilds(Function.identity()),
        GitlabMergeRequestNode::getMergeRequestId
    );

    addNewNodeFromLists(mergeRequests, this.getChilds(GitlabMergeRequestNode::getMergeRequest),
        mr -> new GitlabMergeRequestNode(mr, connectionConfig, projectId));
  }
}
