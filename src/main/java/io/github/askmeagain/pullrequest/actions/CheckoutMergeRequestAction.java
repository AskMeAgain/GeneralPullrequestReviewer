package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import git4idea.repo.GitRepositoryManager;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.services.ManagementService;
import org.jetbrains.annotations.NotNull;

public class CheckoutMergeRequestAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    var ref = "abc";
    var instance = GitRepositoryManager.getInstance(e.getProject());
    var repos = instance.getRepositories();
    int i = 0;
    //GitBrancher.getInstance(e.getProject()).checkout(ref, false, repos, null);
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = ManagementService.getInstance().getTree().getLastSelectedPathComponent();

    e.getPresentation().setEnabled(lastSelectedPathComponent instanceof MergeRequestMarker);
  }
}
