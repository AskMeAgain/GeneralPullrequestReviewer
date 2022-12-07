package io.github.askmeagain.pullrequest.gui.nodes;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.project.Project;
import io.github.askmeagain.pullrequest.dto.TransferKey;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.services.DataRequestService;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FileNodes {

  private final Project project;
  private final String sourceBranch;
  private final String targetBranch;
  private final String filePath;
  private final String mergeRequestId;
  private final String connectionName;


  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  public void openFile() {
    var sourceFile = dataRequestService.getFileOfBranch(connectionName, sourceBranch, filePath);
    var targetFile = dataRequestService.getFileOfBranch(connectionName, targetBranch, filePath);

    var comments = dataRequestService.getCommentsOfPr(connectionName, mergeRequestId);

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
  }

  @Override
  public String toString() {
    return filePath;
  }
}
