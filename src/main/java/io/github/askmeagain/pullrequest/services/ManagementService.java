package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.gui.nodes.RootNode;
import io.github.askmeagain.pullrequest.gui.nodes.gitlab.GitlabConnectionNode;
import io.github.askmeagain.pullrequest.listener.PluginTreeExpansionListener;
import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

@Service
public final class ManagementService {

  @Getter
  private final RootNode rootNode = new RootNode();
  @Getter
  private final Tree tree = new Tree(rootNode);

  private final PluginTreeExpansionListener listener = PluginTreeExpansionListener.getInstance();

  public void refreshList() {
    listener.doWithoutTriggers(rootNode::refresh);
  }

  public static ManagementService getInstance() {
    return ApplicationManager.getApplication().getService(ManagementService.class);
  }

}
