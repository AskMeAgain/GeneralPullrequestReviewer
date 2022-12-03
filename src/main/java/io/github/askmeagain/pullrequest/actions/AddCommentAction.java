package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.gui.MouseClickListener;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class AddCommentAction extends AnAction {

  @Getter(lazy = true)
  private final PluginManagementService pullrequestService = PluginManagementService.getInstance();

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {

    int i = 0;
    //getPullrequestService().addPullrequestComment(null,null);
    var userData = e.getData(CommonDataKeys.EDITOR).getUserData(MouseClickListener.DataContextKeyTarget);
    var userData2 = e.getData(CommonDataKeys.EDITOR).getUserData(MouseClickListener.DataContextKeySource);
  }
}
