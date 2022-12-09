package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;


public class GitlabMergeRequestNode extends BaseTreeNode implements MergeRequestMarker {

  private final String display;
  @Getter
  private final String mergeRequestId;
  private final String sourceBranch;
  private final String targetBranch;
  private final ConnectionConfig connection;
  private final String projectId;

  public GitlabMergeRequestNode(MergeRequest mergeRequest, ConnectionConfig connectionConfig, String projectId) {
    display = mergeRequest.getName();
    mergeRequestId = mergeRequest.getId();
    this.projectId = projectId;
    this.connection = connectionConfig;
    sourceBranch = mergeRequest.getSourceBranch();
    targetBranch = mergeRequest.getTargetBranch();
  }

  private final GitlabService gitlabService = GitlabService.getInstance();

  @Override
  public String toString() {
    return String.format("%s: %s", mergeRequestId, display);
  }

  @Override
  public void onCreation() {
    add(new DefaultMutableTreeNode("hidden"));
  }

  @Override
  public void beforeExpanded() {
    removeAllChildren();

    gitlabService.getFilesOfPr(projectId, connection, mergeRequestId)
        .stream()
        .map(file -> new GitlabFileNode(sourceBranch, targetBranch, file, mergeRequestId, connection, projectId))
        .forEach(this::add);
  }
}
