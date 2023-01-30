package io.github.askmeagain.pullrequest.nodes.gitlab;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.DiscussionNodeMarker;
import io.github.askmeagain.pullrequest.nodes.interfaces.FileNodeMarker;
import io.github.askmeagain.pullrequest.nodes.interfaces.ResolvableMarker;
import io.github.askmeagain.pullrequest.services.EditorService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GitlabFileNode extends BaseTreeNode implements FileNodeMarker, ResolvableMarker {

  private final String sourceBranch;
  private final String targetBranch;
  @Getter
  private final String filePath;
  private final String mergeRequestId;
  private final ConnectionConfig connection;
  private final VcsService gitlabService = GitlabService.getInstance();
  private final String projectId;
  private final DiffHunk fileHunk;

  @Override
  public void onDoubleClick() {
    openFile();
  }

  public void openFile() {
    var sourceFile = gitlabService.getFileOfBranch(projectId, connection, sourceBranch, filePath);
    var targetFile = gitlabService.getFileOfBranch(projectId, connection, targetBranch, filePath);

    var comments = gitlabService.getCommentsOfPr(projectId, connection, mergeRequestId, filePath);

    var sourceComments = comments.stream().filter(MergeRequestDiscussion::isSourceDiscussion).collect(Collectors.toList());
    var targetComments = comments.stream().filter(x -> !x.isSourceDiscussion()).collect(Collectors.toList());

    var sourceReviewFile = ReviewFile.builder()
        .fileContent(sourceFile.getFileContent())
        .fileName(filePath)
        .reviewDiscussions(sourceComments)
        .build();

    var targetReviewFile = ReviewFile.builder()
        .fileContent(targetFile.getFileContent())
        .fileName(filePath)
        .reviewDiscussions(targetComments)
        .build();

    var content1 = DiffContentFactory.getInstance().create(sourceFile.getFileContent());
    var content2 = DiffContentFactory.getInstance().create(targetFile.getFileContent());
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
    request.putUserData(TransferKey.FileNode, this);

    request.putUserData(TransferKey.FileName, filePath);
    request.putUserData(TransferKey.ProjectId, projectId);
    request.putUserData(TransferKey.Connection, connection);
    request.putUserData(TransferKey.MergeRequestId, mergeRequestId);

    request.putUserData(TransferKey.FileHunk, fileHunk);

    var projectId = getActiveProject();

    DiffManager.getInstance().showDiff(projectId, request);

    loadComments(comments);
  }

  @Override
  public void beforeExpanded() {
    var comments = gitlabService.getCommentsOfPr(projectId, connection, mergeRequestId, filePath);
    loadComments(comments);
  }

  @Override
  public void refresh(Object obj) {
    var comments = gitlabService.getCommentsOfPr(projectId, connection, mergeRequestId, filePath);

    var editorService = EditorService.getInstance();
    if (editorService.getDiffView().map(x -> x.getId().equals(filePath)).orElse(false)) {
      editorService.getDiffView().get().refresh(comments);
    }

    removeOrRefreshNodes(
        comments,
        this.getChilds(x -> (GitlabDiscussionNode) x),
        DiscussionNodeMarker::getDiscussion
    );

    addNewNodeFromLists(
        comments,
        this.getChilds(GitlabDiscussionNode::getDiscussion),
        GitlabDiscussionNode::new
    );
  }

  private void loadComments(List<MergeRequestDiscussion> comments) {
    removeAllChildren();

    var discussions = comments.stream()
        .map(GitlabDiscussionNode::new)
        .collect(Collectors.toList());

    discussions.stream()
        .peek(GitlabDiscussionNode::onCreation)
        .forEach(this::add);

    getTreeModel().reload(this);
  }

  @Override
  public String toString() {
    return Path.of(filePath).getFileName().toString();
  }
}