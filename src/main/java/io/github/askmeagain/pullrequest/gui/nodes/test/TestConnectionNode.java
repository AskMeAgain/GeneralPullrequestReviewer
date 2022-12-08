package io.github.askmeagain.pullrequest.gui.nodes.test;

import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.gui.nodes.gitlab.GitlabProjectNode;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Arrays;

@RequiredArgsConstructor
public class TestConnectionNode extends BaseTreeNode {

  private final ConnectionConfig connectionConfig;

  @Override
  public String toString() {
    return connectionConfig.getName();
  }

  @Override
  public void onCreation() {
    add(new TestProjectNode( "CoolProject"));
  }

}
