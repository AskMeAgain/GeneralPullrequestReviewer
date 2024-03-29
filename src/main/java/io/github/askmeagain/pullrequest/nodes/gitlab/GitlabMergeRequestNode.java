package io.github.askmeagain.pullrequest.nodes.gitlab;

import com.intellij.openapi.application.ApplicationManager;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.DiffHunk;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy.Change;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.FakeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GitlabMergeRequestNode extends BaseTreeNode implements MergeRequestMarker {

  @Getter
  private final String url;
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

  @Getter
  private final List<String> reviewerUrls;

  public GitlabMergeRequestNode(MergeRequest mergeRequest, ConnectionConfig connectionConfig, String projectId) {
    this.reviewerUrls = mergeRequest.getReviewer();
    this.projectId = projectId;
    this.mergeRequest = mergeRequest;
    this.connection = connectionConfig;
    this.url = mergeRequest.getUrl();

    display = mergeRequest.getName();
    mergeRequestId = mergeRequest.getId();
    sourceBranch = mergeRequest.getSourceBranch();
    targetBranch = mergeRequest.getTargetBranch();
    canBeMerged = mergeRequest.getApproved();
  }

  private final GitlabService gitlabService = ApplicationManager.getApplication().getService(GitlabService.class);

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

    var filesOfPr = gitlabService.getFilesOfPr(projectId, connection, mergeRequestId);

    var diffHunks = fileHunk(gitlabService.getDiffLegacy(projectId, connection, mergeRequestId));

    removeOrRefreshNodes(filesOfPr, this.getChilds(Function.identity()), GitlabFileNode::getFilePath);
    addNewNodeFromLists(filesOfPr, this.getChilds(GitlabFileNode::getFilePath), file -> new GitlabFileNode(
        sourceBranch,
        targetBranch,
        file,
        mergeRequestId,
        connection,
        projectId,
        diffHunks.get(file)
    ));
  }

  private Map<String, DiffHunk> fileHunk(List<Change> changes) {
    return changes.stream()
        .collect(Collectors.toMap(diffHunk -> diffHunk.getNew_path() == null
            ? diffHunk.getOld_path()
            : diffHunk.getNew_path(), x -> new DiffHunk(x.getDiff())));
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
