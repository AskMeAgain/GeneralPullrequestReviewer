package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import io.github.askmeagain.pullrequest.settings.SettingsGuiConfiguration;
import org.jetbrains.annotations.NotNull;

public class OpenSettingsAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), SettingsGuiConfiguration.class);
  }
}
