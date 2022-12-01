package io.github.askmeagain.pullrequest.gui.nodes;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.project.Project;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.gui.MouseClickListener;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FileNodes {

  private final String prName;
  private final Project project;
  private final String sourceBranch;
  private final String targetBranch;
  private final String filePath;

  @Getter(lazy = true)
  private final PluginManagementService pluginManagementService = PluginManagementService.getInstance();

  public void openFile() {
    var sourceFile = getPluginManagementService().getDataRequestService().getFileOfBranch(sourceBranch, filePath);
    var targetFile = getPluginManagementService().getDataRequestService().getFileOfBranch(targetBranch, filePath);

    var comments = getPluginManagementService().getDataRequestService().getCommentsOfPr();

    comments.clear();

    String commentedSourceFile = injectCommentsIntoFile(sourceFile, comments);
    String commentedTargetFile = injectCommentsIntoFile(targetFile, comments);

    var sourceReviewFile = ReviewFile.builder()
        .fileContent(commentedSourceFile)
        .fileName(sourceBranch)
        .reviewComments(comments)
        .build();

    var targetReviewFile = ReviewFile.builder()
        .fileContent(commentedTargetFile)
        .fileName(targetBranch)
        .build();

    var content1 = DiffContentFactory.getInstance().create(commentedSourceFile);
    var content2 = DiffContentFactory.getInstance().create(commentedTargetFile);
    var request = new SimpleDiffRequest(
        prName,
        content1,
        content2,
        sourceBranch,
        targetBranch
    );

    request.putUserData(MouseClickListener.DataContextKeySource, sourceReviewFile);
    request.putUserData(MouseClickListener.DataContextKeyTarget, targetReviewFile);

    DiffManager.getInstance().showDiff(project, request);
  }

  private String injectCommentsIntoFile(String file, List<ReviewComment> commentsOfPr) {
    var splittedFile = new ArrayList<>(List.of(file.split("\n")));

    commentsOfPr.stream()
        .sorted((l, r) -> -1 * Integer.compare(l.getLine(), r.getLine()))
        .forEach(comment -> splittedFile.add(comment.getLine(), comment.getText()));

    return String.join("\n", splittedFile);
  }

  @Override
  public String toString() {
    return prName;
  }
}
