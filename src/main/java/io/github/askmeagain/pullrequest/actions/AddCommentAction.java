package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import io.github.askmeagain.pullrequest.dto.application.CommentRequest;
import io.github.askmeagain.pullrequest.gui.MouseClickListener;
import io.github.askmeagain.pullrequest.gui.dialogs.AddCommentDialog;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class AddCommentAction extends AnAction {

  @Getter(lazy = true)
  private final PluginManagementService pullrequestService = PluginManagementService.getInstance();

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);

    var isSource = e.getData(CommonDataKeys.EDITOR).getUserData(MouseClickListener.IsSource);

    editor.getCaretModel().runForEachCaret(caret -> {

      var end = caret.getSelectionEnd();

      var line = editor.offsetToLogicalPosition(end).line;

      new AddCommentDialog(text -> getPullrequestService().addPullrequestComment("1", CommentRequest.builder()
          .oldFileName("README.md")
          .newFileName("README.md")
          .sourceComment(isSource)
          .text(text)
          .line(line)
          .build()))
          .show();

    });
  }
}
