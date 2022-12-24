package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.askmeagain.pullrequest.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.services.ManagementService;
import org.jetbrains.annotations.NotNull;

public class ApproveMergeRequestAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = ManagementService.getInstance().getTree().getLastSelectedPathComponent();
    if (lastSelectedPathComponent instanceof MergeRequestMarker) {
      var casted = (MergeRequestMarker) lastSelectedPathComponent;
      casted.approveMergeRequest();
    }
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = ManagementService.getInstance().getTree().getLastSelectedPathComponent();
    e.getPresentation().setEnabled(lastSelectedPathComponent instanceof MergeRequestMarker);
  }
}
