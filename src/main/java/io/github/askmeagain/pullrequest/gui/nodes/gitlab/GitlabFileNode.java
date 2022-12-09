package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.FileNodeMarker;
import io.github.askmeagain.pullrequest.listener.PluginTreeExpansionListener;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GitlabFileNode extends BaseTreeNode implements FileNodeMarker {

  private final String sourceBranch;
  private final String targetBranch;
  private final String filePath;
  private final String mergeRequestId;
  private final ConnectionConfig connection;

  private final GitlabService gitlabService = GitlabService.getInstance();

  private final PluginTreeExpansionListener listener = PluginTreeExpansionListener.getInstance();

  @Override
  public void onClick() {
    var sourceFile = gitlabService.getFileOfBranch(connection, sourceBranch, filePath);
    var targetFile = gitlabService.getFileOfBranch(connection, targetBranch, filePath);

    var comments = gitlabService.getCommentsOfPr(connection, mergeRequestId);

    var sourceComments = comments.stream().filter(MergeRequestDiscussion::isSourceDiscussion).collect(Collectors.toList());
    var targetComments = comments.stream().filter(x -> !x.isSourceDiscussion()).collect(Collectors.toList());

    var sourceReviewFile = ReviewFile.builder()
        .fileContent(sourceFile)
        .fileName(sourceBranch)
        .reviewDiscussions(sourceComments)
        .build();

    var targetReviewFile = ReviewFile.builder()
        .fileContent(targetFile)
        .fileName(targetBranch)
        .reviewDiscussions(targetComments)
        .build();

    var content1 = DiffContentFactory.getInstance().create(sourceFile);
    var content2 = DiffContentFactory.getInstance().create(targetFile);
    var request = new SimpleDiffRequest(
        filePath,
        content2,
        content1,
        targetBranch,
        sourceBranch
    );

    request.putUserData(TransferKey.AllDiscussions, comments);

    request.putUserData(TransferKey.DataContextKeySource, sourceReviewFile);
    request.putUserData(TransferKey.DataContextKeyTarget, targetReviewFile);

    request.putUserData(TransferKey.FileName, filePath);
    request.putUserData(TransferKey.Connection, connection);
    request.putUserData(TransferKey.MergeRequestId, mergeRequestId);

    var projectId = getActiveProject();

    DiffManager.getInstance().showDiff(projectId, request);

    loadComments(comments);
  }

  @Override
  public void beforeExpanded() {
    var comments = gitlabService.getCommentsOfPr(connection, mergeRequestId);
    loadComments(comments);
  }

  @Override
  public void refresh() {
    super.refresh();
    var comments = gitlabService.getCommentsOfPr(connection, mergeRequestId);
    loadComments(comments);
  }

  private void loadComments(List<MergeRequestDiscussion> comments) {
    removeAllChildren();
    comments.stream()
        .map(GitlabDiscussionNode::new)
        .peek(GitlabDiscussionNode::onCreation)
        .forEach(this::add);

    //TODO
    //listener.doWithoutTriggers(() -> tree.expandPath(new TreePath(this.getPath())));
  }

  @Override
  public String toString() {
    return filePath;
  }

}
