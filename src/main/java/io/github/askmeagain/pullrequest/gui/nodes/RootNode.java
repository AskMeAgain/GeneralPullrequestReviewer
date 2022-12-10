package io.github.askmeagain.pullrequest.gui.nodes;

import com.intellij.openapi.application.ApplicationManager;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.gui.nodes.gitlab.GitlabConnectionNode;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.ConnectionMarker;
import io.github.askmeagain.pullrequest.gui.nodes.interfaces.NodeBehaviour;
import io.github.askmeagain.pullrequest.services.ManagementService;
import io.github.askmeagain.pullrequest.services.StateService;

import javax.swing.event.TreeModelEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

public class RootNode extends BaseTreeNode {

  private final PullrequestPluginState state = StateService.getInstance().getState();

  public RootNode() {
    onCreation();
  }

  @Override
  public void refresh() {

    var connectionSet = new HashSet<>(state.getConnectionConfigs());
    var childs = this.getChilds(ConnectionMarker::getConnection);
    var childSet = new HashSet<>(childs);

    for (var child : this.<ConnectionMarker, ConnectionMarker>getChilds(Function.identity())) {
      //connection was there
      if (connectionSet.contains(child.getConnection())) {
        var casted = (NodeBehaviour) child;
        casted.refresh();
      } else {
        getTreeModel().removeNodeFromParent(child);
        getTreeModel().reload(this);
      }
    }

    //add new!
    for (var newConnectionConfig : state.getConnectionConfigs()) {
      if (!childSet.contains(newConnectionConfig)) {
        var newChild = new GitlabConnectionNode(newConnectionConfig);
        var treeModel = getTreeModel();
        var childCount = this.getChildCount();
        treeModel.insertNodeInto(newChild, this, childCount);
        newChild.onCreation();
        treeModel.reload(this);
      }
    }
  }

  @Override
  public void onCreation() {
    state.getConnectionConfigs()
        .stream()
        .map(GitlabConnectionNode::new)
        .peek(GitlabConnectionNode::onCreation)
        .forEach(this::add);
  }
}
