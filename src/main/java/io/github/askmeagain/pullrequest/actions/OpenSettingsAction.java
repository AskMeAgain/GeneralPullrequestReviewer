package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.ConnectionMarker;
import io.github.askmeagain.pullrequest.services.ManagementService;
import io.github.askmeagain.pullrequest.settings.SettingsGuiService;
import org.jetbrains.annotations.NotNull;

public class OpenSettingsAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var settings = SettingsGuiService.getInstance();
    var nodes = ManagementService.getInstance().getTree().getSelectionPath().getPath();
    for (int i = 0; i < nodes.length; i++) {
      if (nodes[i] instanceof ConnectionMarker) {
        var casted = (ConnectionMarker) nodes[i];
        settings.setSelectedTab(casted.getConnection());
      }
    }

    ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), settings);
  }
}
