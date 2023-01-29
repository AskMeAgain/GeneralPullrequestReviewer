package io.github.askmeagain.pullrequest.services.vcs;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.ThrowableComputable;
import io.github.askmeagain.pullrequest.dto.application.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;

import javax.swing.*;
import java.util.List;

@RequiredArgsConstructor
public class VcsServiceProgressionProxy implements VcsService {

  private final VcsService vcsService;

  @Override
  public List<MergeRequest> getMergeRequests(String projectId, ConnectionConfig connectionName) {
    return vcsService.getMergeRequests(projectId, connectionName);
  }

  @Override
  public void addCommentToThread(String projectId, ConnectionConfig connectionName, String mergeRequestId, String discussionId, String body) {
    withProgress(() -> {
      vcsService.addCommentToThread(projectId, connectionName, mergeRequestId, discussionId, body);
      return null;
    }, "Adding comment to thread");
  }

  public void editComment(ConnectionConfig connectionName, String projectId, String mergeRequestId, String discussionId, String note_id, String body) {
    withProgress(() -> {
      vcsService.editComment(connectionName, projectId, mergeRequestId, discussionId, note_id, body);
      return null;
    }, "Edit gitlab comment");
  }

  public void deleteComment(ConnectionConfig connectionName, String projectId, String mergeRequestId, String discussionId, String note_id) {
    withProgress(() -> {
      vcsService.deleteComment(connectionName, projectId, mergeRequestId, discussionId, note_id);
      return true;
    }, "Delete gitlab comment");
  }

  @Override
  public List<String> getFilesOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId) {
    return withProgress(() -> vcsService.getFilesOfPr(projectId, connectionName, mergeRequestId), "Fetching files of PR");
  }

  @Override
  public List<MergeRequestDiscussion> getCommentsOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId, String file) {
    return withProgress(() -> vcsService.getCommentsOfPr(projectId, connectionName, mergeRequestId, file), "Fetching comments of PR");
  }

  @Override
  public FileResponse getFileOfBranch(String projectId, ConnectionConfig connectionName, String branch, String filePath) {
    return withProgressWithoutDebug(() -> vcsService.getFileOfBranch(projectId, connectionName, branch, filePath), "Fetching files of branch");
  }

  @Override
  public void addMergeRequestComment(String projectId, ConnectionConfig connectionName, String mergeRequestId, CommentRequest comment) {
    withProgress(() -> {
      vcsService.addMergeRequestComment(projectId, connectionName, mergeRequestId, comment);
      return null;
    }, "Add comment");
  }

  @Override
  public void approveMergeRequest(String projectId, ConnectionConfig connection, String mergeRequestId) {
    withProgress(() -> {
      vcsService.approveMergeRequest(projectId, connection, mergeRequestId);
      return null;
    }, "Approve merge request");
  }

  @Override
  public ProjectResponse getProject(ConnectionConfig connection, String projectId) {
    return withProgress(() -> vcsService.getProject(connection, projectId), "Fetching project information");
  }

  @Override
  public String getDiffHunk(String projectId, ConnectionConfig connection, String mergeRequestId) {
    return withProgress(() -> vcsService.getDiffHunk(projectId, connection, mergeRequestId), "Fetching diff hunk");
  }

  @Override
  public void resolveComment() {
    throw new NotImplementedException("Not implemented");
  }

  private <T> T withProgress(ThrowableComputable<T, Exception> runnable, String title) {
    try {
      return ProgressManager.getInstance().runProcessWithProgressSynchronously(
          runnable,
          title,
          false,
          null
      );
    } catch (Exception e) {
      JOptionPane.showMessageDialog(null, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private <T> T withProgressWithoutDebug(ThrowableComputable<T, Exception> runnable, String title) {
    try {
      return ProgressManager.getInstance().runProcessWithProgressSynchronously(
          runnable,
          title,
          false,
          null
      );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
