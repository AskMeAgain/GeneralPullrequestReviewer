package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.askmeagain.pullrequest.nodes.interfaces.DiscussionNodeMarker;
import io.github.askmeagain.pullrequest.services.EditorService;
import io.github.askmeagain.pullrequest.services.ManagementService;
import org.jetbrains.annotations.NotNull;

public class GoToDiscussionAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = (DiscussionNodeMarker) ManagementService.getInstance()
        .getTree()
        .getLastSelectedPathComponent();

    var discussion = lastSelectedPathComponent.getDiscussion();

    EditorService.getInstance().getDiffView().ifPresent(view -> {
      view.scrolleToDiscussion(discussion);
    });
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = ManagementService.getInstance().getTree().getLastSelectedPathComponent();

    e.getPresentation().setEnabled(lastSelectedPathComponent instanceof DiscussionNodeMarker);
  }
}
