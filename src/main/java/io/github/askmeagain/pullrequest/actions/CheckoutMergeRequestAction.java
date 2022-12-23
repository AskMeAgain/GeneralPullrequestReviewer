package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import git4idea.repo.GitRepositoryManager;
import io.github.askmeagain.pullrequest.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.services.ManagementService;
import org.jetbrains.annotations.NotNull;

public class CheckoutMergeRequestAction extends AnAction {
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    //TODO to be implemented
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
