package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.NodeBehaviour;
import io.github.askmeagain.pullrequest.services.ManagementService;
import org.jetbrains.annotations.NotNull;

public class RefreshNodeAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = (NodeBehaviour) ManagementService.getInstance()
        .getTree()
        .getLastSelectedPathComponent();

    lastSelectedPathComponent.refresh();
  }
}
