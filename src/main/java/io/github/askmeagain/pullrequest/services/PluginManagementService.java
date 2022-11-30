package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import lombok.Getter;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

@Service
public final class PluginManagementService {

  @Getter(lazy = true)
  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  private final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
  @Getter
  private final Tree tree = new Tree(rootNode);

  public void refreshList() {
    System.out.println("Refresh list!");

    rootNode.removeAllChildren();

    getDataRequestService().getMergeRequests().forEach(pr -> {
      var prNode = new DefaultMutableTreeNode(pr);
      rootNode.add(prNode);
      //hacky node
      prNode.add(new DefaultMutableTreeNode("this will not be seen"));
    });

    var model = (DefaultTreeModel)tree.getModel();
    model.reload();
  }

  public static PluginManagementService getInstance() {
    return ApplicationManager.getApplication().getService(PluginManagementService.class);
  }

}
