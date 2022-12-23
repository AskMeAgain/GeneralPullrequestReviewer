package io.github.askmeagain.pullrequest.nodes.test;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.vcs.test.TestService;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TestFileNode extends BaseTreeNode {

  private final String sourceBranch;
  private final String targetBranch;
  private final String filePath;
  private final String mergeRequestId;
  private final TestService testService = TestService.getInstance();

  @Override
  public void onClick() {
    var sourceFile = testService.getFileOfBranch(null, null, sourceBranch, filePath);
    var targetFile = testService.getFileOfBranch(null, null, targetBranch, filePath);

    var comments = testService.getCommentsOfPr(null, null, mergeRequestId);

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
    request.putUserData(TransferKey.MergeRequestId, mergeRequestId);

    var projectId = getActiveProject();

    DiffManager.getInstance().showDiff(projectId, request);

  }

  @Override
  public String toString() {
    return filePath;
  }

}

