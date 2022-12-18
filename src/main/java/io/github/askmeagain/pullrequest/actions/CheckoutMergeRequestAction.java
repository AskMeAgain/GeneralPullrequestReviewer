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
    //var repos = instance.getRepositories();
    int i = 0;
    //
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = ManagementService.getInstance().getTree().getLastSelectedPathComponent();

    e.getPresentation().setEnabled(false);

    if (lastSelectedPathComponent instanceof MergeRequestMarker) {
      var casted = (MergeRequestMarker) lastSelectedPathComponent;
      var instance = GitRepositoryManager.getInstance(e.getProject());

      var gitRepository = instance.getRepositories().get(0);
      var hasBranch = gitRepository.getBranches().findBranchByName(casted.getTargetBranchName());

      if (hasBranch != null) {
        e.getPresentation().setEnabled(true);
      }
    }
  }
}
