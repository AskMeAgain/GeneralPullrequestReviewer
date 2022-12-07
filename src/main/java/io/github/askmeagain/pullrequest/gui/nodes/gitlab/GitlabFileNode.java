package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.project.Project;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.TransferKey;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.TreePath;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GitlabFileNode extends BaseTreeNode {

  private final Project project;
  private final String sourceBranch;
  private final String targetBranch;
  private final String filePath;
  private final String mergeRequestId;
  private final String connectionName;
  private final Tree tree;

  private final GitlabService gitlabService = GitlabService.getInstance();

  @Override
  public void onClick() {
    var sourceFile = gitlabService.getFileOfBranch(connectionName, sourceBranch, filePath);
    var targetFile = gitlabService.getFileOfBranch(connectionName, targetBranch, filePath);

    var comments = gitlabService.getCommentsOfPr(connectionName, mergeRequestId);

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
    request.putUserData(TransferKey.ConnectionName, connectionName);
    request.putUserData(TransferKey.MergeRequestId, mergeRequestId);

    DiffManager.getInstance().showDiff(project, request);

    comments.stream()
        .map(DiscussionNode::new)
        .peek(DiscussionNode::onCreation)
        .forEach(this::add);

    tree.expandPath(new TreePath(this.getPath()));
  }

  @Override
  public String toString() {
    return filePath;
  }
}

