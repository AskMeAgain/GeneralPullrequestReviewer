package io.github.askmeagain.pullrequest.nodes.github;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.FileNodeMarker;
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
  @Getter
  private final String filePath;
  private final String mergeRequestId;
  private final ConnectionConfig connection;
  private final GithubService githubService = GithubService.getInstance();
  private final String projectId;

  public void openFile() {
    var sourceFile = getFileOfBranchOrDefault("", sourceBranch);
    var targetFile = getFileOfBranchOrDefault("", targetBranch);

    var comments = githubService.getCommentsOfPr(projectId, connection, mergeRequestId, filePath);

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
    request.putUserData(TransferKey.ProjectId, projectId);
    request.putUserData(TransferKey.Connection, connection);
    request.putUserData(TransferKey.MergeRequestId, mergeRequestId);

    var projectId = getActiveProject();

    DiffManager.getInstance().showDiff(projectId, request);

    loadComments(comments);
  }

  @NotNull
  private String getFileOfBranchOrDefault(String branch, String defaultValue) {
    try {
      return githubService.getFileOfBranch(projectId, connection, branch, filePath);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  @Override
  public void beforeExpanded() {
    var comments = githubService.getCommentsOfPr(projectId, connection, mergeRequestId, filePath);
    loadComments(comments);
  }

  @Override
  public void refresh() {
    super.refresh();
    var comments = githubService.getCommentsOfPr(projectId, connection, mergeRequestId, filePath);
    loadComments(comments);
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

