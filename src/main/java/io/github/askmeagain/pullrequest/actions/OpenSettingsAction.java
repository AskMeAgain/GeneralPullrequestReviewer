package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.ConnectionMarker;
import io.github.askmeagain.pullrequest.services.ManagementService;
import io.github.askmeagain.pullrequest.services.SettingsGuiService;
import io.github.askmeagain.pullrequest.services.StateService;
import org.jetbrains.annotations.NotNull;

public class OpenSettingsAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var selectionPath = ManagementService.getInstance().getTree().getSelectionPath();
    if (selectionPath != null) {
      var nodes = selectionPath.getPath();
      for (var node : nodes) {
        if (node instanceof ConnectionMarker) {
          var casted = (ConnectionMarker) node;
          StateService.getInstance().setSelectedTab(casted.getConnection().getName());
        }
      }
    }

    ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), SettingsGuiService.class);
  }
}
