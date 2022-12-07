package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.gui.nodes.gitlab.GitlabConnectionNode;
import lombok.Getter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

@Service
public final class ManagementService {

  private final PullrequestPluginState state = StateService.getInstance().getState();

  private final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
  @Getter
  private final Tree tree = new Tree(rootNode);

  public ManagementService() {
    init();
  }

  public void refreshList() {
    rootNode.removeAllChildren();
    init();
  }

  private void init() {
    state.getConnectionConfigs()
        .stream()
        .map(connection -> new GitlabConnectionNode(connection, tree))
        .peek(GitlabConnectionNode::onCreation)
        .forEach(rootNode::add);

    var model = (DefaultTreeModel) tree.getModel();
    model.reload();
  }

  public static ManagementService getInstance() {
    return ApplicationManager.getApplication().getService(ManagementService.class);
  }

}
