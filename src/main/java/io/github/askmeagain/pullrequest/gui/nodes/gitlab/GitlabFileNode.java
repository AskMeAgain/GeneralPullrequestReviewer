package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GitlabFileNode extends BaseTreeNode {

  private final String sourceBranch;
  private final String targetBranch;
  private final String filePath;
  private final String mergeRequestId;
  private final ConnectionConfig connection;
  private final Tree tree;

  private final GitlabService gitlabService = GitlabService.getInstance();

  private boolean doExpand = true;

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
    if (doExpand) {
      var comments = gitlabService.getCommentsOfPr(connection, mergeRequestId);
      loadComments(comments);
    }
  }

  @Override
  public void refresh() {
    super.refresh();
  }

  private void loadComments(List<MergeRequestDiscussion> comments) {
    removeAllChildren();
    comments.stream()
        .map(GitlabDiscussionNode::new)
        .peek(GitlabDiscussionNode::onCreation)
        .forEach(this::add);

    doExpand = false;
    tree.expandPath(new TreePath(this.getPath()));
    doExpand = true;
  }

  @Override
  public String toString() {
    return filePath;
  }

  private Project getActiveProject() {
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    for (Project project : projects) {
      Window window = WindowManager.getInstance().suggestParentWindow(project);
      if (window != null && window.isActive()) {
        return project;
      }
    }
    throw new RuntimeException("Could not find active project");
  }
}

