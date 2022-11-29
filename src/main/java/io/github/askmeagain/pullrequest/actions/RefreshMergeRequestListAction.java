package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.askmeagain.pullrequest.service.PluginStateService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class RefreshMergeRequestListAction extends AnAction {

  @Getter(lazy = true)
  private final PluginStateService pullrequestService = PluginStateService.getInstance();

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    getPullrequestService().refreshList();
  }
}
