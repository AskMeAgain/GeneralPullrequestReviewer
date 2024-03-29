package io.github.askmeagain.pullrequest.nodes.github;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.DiscussionNodeMarker;
import io.github.askmeagain.pullrequest.nodes.interfaces.FileNodeMarker;
import io.github.askmeagain.pullrequest.services.EditorService;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;
import io.github.askmeagain.pullrequest.services.vcs.github.GithubService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GithubFileNode extends BaseTreeNode implements FileNodeMarker {
  private final String sourceBranch;
  private final String targetBranch;
  private final String commitId;
  @Getter
  private final String filePath;
  private final String mergeRequestId;
  private final ConnectionConfig connection;
  private final VcsService githubService = GithubService.getInstance();
  private final String projectId;
  private final DiffHunk fileHunk;

  public void openFile() {
    var sourceFile = getFileOfBranchOrDefault(sourceBranch);
    var targetFile = getFileOfBranchOrDefault(targetBranch);

    var comments = githubService.getCommentsOfPr(projectId, connection, mergeRequestId, filePath);

    var sourceComments = comments.stream().filter(MergeRequestDiscussion::isSourceDiscussion).collect(Collectors.toList());
    var targetComments = comments.stream().filter(x -> !x.isSourceDiscussion()).collect(Collectors.toList());

    var sourceReviewFile = ReviewFile.builder()
        .fileContent(sourceFile.getFileContent())
        .branch(sourceBranch)
        .fileName(filePath)
        .reviewDiscussions(sourceComments)
        .build();

    var targetReviewFile = ReviewFile.builder()
        .fileContent(targetFile.getFileContent())
        .branch(targetBranch)
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
    request.putUserData(TransferKey.CommitId, commitId);

    request.putUserData(TransferKey.FileHunk, fileHunk);

    var projectId = getActiveProject();

    DiffManager.getInstance().showDiff(projectId, request);

    loadComments(comments);
  }

  @NotNull
  private FileResponse getFileOfBranchOrDefault(String branch) {
    try {
      return githubService.getFileOfBranch(projectId, connection, branch, filePath);
    } catch (Exception e) {
      return FileResponse.builder()
          .fileContent("")
          .build();
    }
  }

  @Override
  public void beforeExpanded() {
    var comments = githubService.getCommentsOfPr(projectId, connection, mergeRequestId, filePath);
    loadComments(comments);
  }

  @Override
  public void onDoubleClick() {
    openFile();
  }

  @Override
  public void refresh(Object obj) {
    var comments = githubService.getCommentsOfPr(projectId, connection, mergeRequestId, filePath);

    var editorService = EditorService.getInstance();
    if (editorService.getDiffView().map(x -> x.getId().equals(filePath)).orElse(false)) {
      editorService.getDiffView().get().refresh(comments);
    }

    removeOrRefreshNodes(
        comments,
        this.getChilds(x -> (GithubDiscussionNode) x),
        DiscussionNodeMarker::getDiscussion
    );

    addNewNodeFromLists(
        comments,
        this.getChilds(GithubDiscussionNode::getDiscussion),
        GithubDiscussionNode::new
    );
  }

  private void loadComments(List<MergeRequestDiscussion> comments) {
    removeAllChildren();
    comments.stream()
        .map(GithubDiscussionNode::new)
        .peek(GithubDiscussionNode::onCreation)
        .forEach(this::add);
  }

  @Override
  public String toString() {
    return Path.of(filePath).getFileName().toString();
  }

}

