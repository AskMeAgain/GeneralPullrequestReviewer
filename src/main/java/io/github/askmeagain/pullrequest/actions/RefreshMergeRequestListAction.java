package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class RefreshMergeRequestListAction extends AnAction {

  @Getter(lazy = true)
  private final PluginManagementService pullrequestService = PluginManagementService.getInstance();

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    getPullrequestService().refreshList();
  }
}
