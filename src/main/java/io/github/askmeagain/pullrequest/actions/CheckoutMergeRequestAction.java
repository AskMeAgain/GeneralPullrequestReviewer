package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.askmeagain.pullrequest.services.ManagementService;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class CheckoutMergeRequestAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    ManagementService.getInstance().getRootNode().removeAllChildren();
    var model = (DefaultTreeModel) ManagementService.getInstance().getTree().getModel();
    model.reload();
  }
}
