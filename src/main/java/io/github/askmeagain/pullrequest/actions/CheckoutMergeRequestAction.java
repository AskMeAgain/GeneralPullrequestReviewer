package io.github.askmeagain.pullrequest.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import git4idea.repo.GitRepositoryManager;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
    var instance = GitRepositoryManager.getInstance(e.getProject());
    var repos = instance.getRepositories().get(0);

    var editor = e.getRequiredData(CommonDataKeys.EDITOR);

    var source = editor.getUserData(TransferKey.SourceBranch);

    var currentBranch = repos.getCurrentBranch();
    if (Objects.equals(currentBranch.getName(), source)) {
      e.getPresentation().setEnabled(false);
    }


  }
}
