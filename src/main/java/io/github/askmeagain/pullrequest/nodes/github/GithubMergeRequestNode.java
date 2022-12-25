package io.github.askmeagain.pullrequest.nodes.github;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.FakeNode;
import io.github.askmeagain.pullrequest.nodes.gitlab.GitlabFileNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.Getter;

import java.util.function.Function;


public class GithubMergeRequestNode extends BaseTreeNode implements MergeRequestMarker {

  private final String display;
  @Getter
  private final String mergeRequestId;
  private final String sourceBranch;
  private final String targetBranch;
  private final ConnectionConfig connection;
  private final String projectId;

  @Getter
  private final Boolean canBeMerged;

  @Getter
  private final MergeRequest mergeRequest;

  public GithubMergeRequestNode(MergeRequest mergeRequest, ConnectionConfig connectionConfig, String projectId) {
    display = mergeRequest.getName();
    mergeRequestId = mergeRequest.getId();
    this.projectId = projectId;
    this.mergeRequest = mergeRequest;
    this.connection = connectionConfig;
    sourceBranch = mergeRequest.getSourceBranch();
    targetBranch = mergeRequest.getTargetBranch();
    canBeMerged = mergeRequest.getApproved();
  }

  private final GitlabService gitlabService = GitlabService.getInstance();

  @Override
  public String toString() {
    return String.format("%s: %s", mergeRequestId, display);
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

    var filesOfPr = gitlabService.getFilesOfPr(projectId, connection, mergeRequestId);

    removeOrRefreshNodes(filesOfPr, this.getChilds(Function.identity()), GitlabFileNode::getFilePath);
    addNewNodeFromLists(filesOfPr, this.getChilds(GitlabFileNode::getFilePath), file -> new GitlabFileNode(
        sourceBranch,
        targetBranch,
        file,
        mergeRequestId,
        connection,
        projectId
    ));
  }

  @Override
  public String getSourceBranch() {
    return targetBranch;
  }

  @Override
  public void approveMergeRequest() {
    GitlabService.getInstance().approveMergeRequest(projectId, connection, mergeRequestId);
  }

}
