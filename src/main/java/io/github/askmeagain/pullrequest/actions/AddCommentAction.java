package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import io.github.askmeagain.pullrequest.dto.application.CommentRequest;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.gui.dialogs.AddCommentDialog;
import io.github.askmeagain.pullrequest.services.DataRequestService;
import org.jetbrains.annotations.NotNull;

public class AddCommentAction extends AnAction {

  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);
    var projectId = editor.getUserData(TransferKey.ProjectId);
    var connection = editor.getUserData(TransferKey.Connection);
    editor.getCaretModel()
        .runForEachCaret(caret -> new AddCommentDialog(text -> dataRequestService.getMapVcsImplementation()
            .get(connection.getVcsImplementation())
            .addMergeRequestComment(
                projectId,
                connection,
                editor.getUserData(TransferKey.MergeRequestId),
                CommentRequest.builder()
                    .text(text)
                    .commitId(editor.getUserData(TransferKey.CommitId))
                    .oldFileName(editor.getUserData(TransferKey.FileName))
                    .newFileName(editor.getUserData(TransferKey.FileName))
                    .sourceComment(editor.getUserData(TransferKey.IsSource))
                    .lineStart(editor.offsetToLogicalPosition(caret.getSelectionStart()).line)
                    .lineEnd(editor.offsetToLogicalPosition(caret.getSelectionEnd()).line)
                    .build()))
            .show());
  }
}
