package io.github.askmeagain.pullrequest.nodes.github;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.DiffHunk;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.FakeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.github.GithubService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GithubMergeRequestNode extends BaseTreeNode implements MergeRequestMarker {
  @Getter
  private final String url;
  private final String display;
  @Getter
  private final String mergeRequestId;
  private final String sourceBranch;
  private final String targetBranch;
  private final ConnectionConfig connection;
  private final String projectId;

  private final String commitId;

  @Getter
  private final List<String> reviewerUrls;

  @Getter
  private final Boolean canBeMerged;

  @Getter
  private final MergeRequest mergeRequest;

  public GithubMergeRequestNode(MergeRequest mergeRequest, ConnectionConfig connectionConfig, String projectId) {
    display = mergeRequest.getName();
    this.commitId = mergeRequest.getCommitSha();
    mergeRequestId = mergeRequest.getId();
    this.projectId = projectId;
    this.mergeRequest = mergeRequest;
    this.connection = connectionConfig;
    this.reviewerUrls = mergeRequest.getReviewer();
    this.url = mergeRequest.getUrl();
    sourceBranch = mergeRequest.getSourceBranch();
    targetBranch = mergeRequest.getTargetBranch();
    canBeMerged = mergeRequest.getApproved();
  }

  private final VcsService githubService = GithubService.getInstance();

  @Override
  public String toString() {
    return String.format("%s: %s", mergeRequestId, display);
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

    var filesOfPr = githubService.getFilesOfPr(projectId, connection, mergeRequestId);

    var diffHunks = fileHunk(githubService.getDiffHunk(projectId, connection, mergeRequestId));

    removeOrRefreshNodes(filesOfPr, this.getChilds(Function.identity()), GithubFileNode::getFilePath);
    addNewNodeFromLists(filesOfPr, this.getChilds(GithubFileNode::getFilePath), file -> new GithubFileNode(
        sourceBranch,
        targetBranch,
        commitId,
        file,
        mergeRequestId,
        connection,
        projectId,
        diffHunks.getOrDefault(file, diffHunks.get(file))
    ));
  }

  private Map<String, DiffHunk> fileHunk(String hunk) {
    return Arrays.stream(hunk.split("diff --git"))
        .filter(StringUtils::isNotBlank)
        .map(DiffHunk::new)
        .collect(Collectors.toMap(diffHunk -> diffHunk.getSourceFileName() == null
            ? diffHunk.getSourceFileName()
            : diffHunk.getTargetFileName(), Function.identity()));
  }

  @Override
  public String getSourceBranch() {
    return targetBranch;
  }

  @Override
  public void approveMergeRequest() {
    GithubService.getInstance().approveMergeRequest(projectId, connection, mergeRequestId);
  }
}
