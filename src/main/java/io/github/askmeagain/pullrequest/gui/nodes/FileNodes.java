package io.github.askmeagain.pullrequest.gui.nodes;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.project.Project;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.gui.MouseClickListener;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FileNodes {

  private final String prName;
  private final Project project;
  private final String sourceBranch;
  private final String targetBranch;
  private final String filePath;
  private final String mergeRequestId;

  @Getter(lazy = true)
  private final PluginManagementService pluginManagementService = PluginManagementService.getInstance();

  public void openFile() {
    var sourceFile = getPluginManagementService().getDataRequestService().getFileOfBranch(sourceBranch, filePath);
    var targetFile = getPluginManagementService().getDataRequestService().getFileOfBranch(targetBranch, filePath);

    var comments = getPluginManagementService().getDataRequestService().getCommentsOfPr(mergeRequestId);

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
        prName,
        content2,
        content1,
        targetBranch,
        sourceBranch
    );

    request.putUserData(MouseClickListener.DataContextKeySource, sourceReviewFile);
    request.putUserData(MouseClickListener.DataContextKeyTarget, targetReviewFile);

    request.putUserData(MouseClickListener.FileName, filePath);
    request.putUserData(MouseClickListener.MergeRequestId, mergeRequestId);

    DiffManager.getInstance().showDiff(project, request);
  }

  @Override
  public String toString() {
    return prName;
  }
}
