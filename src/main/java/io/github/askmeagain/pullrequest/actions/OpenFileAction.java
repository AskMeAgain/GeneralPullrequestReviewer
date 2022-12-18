package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.FileNodeMarker;
import io.github.askmeagain.pullrequest.services.ManagementService;
import org.jetbrains.annotations.NotNull;

public class OpenFileAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = (FileNodeMarker) ManagementService.getInstance()
        .getTree()
        .getLastSelectedPathComponent();

    lastSelectedPathComponent.openFile();
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = ManagementService.getInstance().getTree().getLastSelectedPathComponent();

    e.getPresentation().setEnabled(lastSelectedPathComponent instanceof FileNodeMarker);
  }
}
