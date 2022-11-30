package io.github.askmeagain.pullrequest.windows;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PullrequestToolWindow implements ToolWindowFactory, DumbAware {

  public static final @NotNull Key<MergeRequest> DataContextKey = Key.create("selectedFileIndex");

  @Getter(lazy = true)
  private final PluginManagementService pluginManagementService = PluginManagementService.getInstance();

  @Override
  public void createToolWindowContent(Project project, ToolWindow toolWindow) {

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1;
    gbc.weighty = 0.5;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;

    var panel = new JPanel(new GridBagLayout());
    panel.add(createPanel(panel, project), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;

    var content = ApplicationManager.getApplication()
        .getService(ContentFactory.class)
        .createContent(panel, "", false);
    var contentManager = toolWindow.getContentManager();

    contentManager.addContent(content);
  }

  private JComponent createPanel(JPanel parent, Project project) {
    var buttonToolBar = createButtonToolBar(parent);

    var tree = getPluginManagementService().getTree();
    tree.setRootVisible(false);

    var flag = new AtomicBoolean(false);

    tree.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent me) {
        TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
        if (tp != null) {
          var lastPathComponent = (DefaultMutableTreeNode) tp.getLastPathComponent();
          if (lastPathComponent.getChildCount() == 0) {
            openDiffWindow(lastPathComponent, project);
          }
        }
      }
    });

    tree.addTreeExpansionListener(new TreeExpansionListener() {
      @Override
      public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
        if (flag.get()) {
          return;
        }
        var lastNode = (DefaultMutableTreeNode) treeExpansionEvent.getPath().getLastPathComponent();
        lastNode.removeAllChildren();
        //get files now
        getPluginManagementService().getDataRequestService().getFilesOfPr().forEach(file -> {
          var newChild = new DefaultMutableTreeNode(file);
          lastNode.add(newChild);
        });
        var model = (DefaultTreeModel) tree.getModel();
        model.reload();
        flag.set(true);
        tree.expandPath(treeExpansionEvent.getPath());
        flag.set(false);
      }

      @Override
      public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {
      }
    });

    return createListPanel(buttonToolBar, tree);
  }

  private void openDiffWindow(DefaultMutableTreeNode lastPathComponent, Project project) {
    var prName = (String) lastPathComponent.getUserObject();

    //var splittedFile = new ArrayList<>(List.of(reviewFile.getFileContent().split("\n")));

//    getPluginManagementService().getDataRequestService().getCommentsOfPr()
//        .stream()
//        .sorted((l, r) -> -1 * Integer.compare(l.getLine(), r.getLine()))
//        .forEach(comment -> splittedFile.add(comment.getLine(), comment.getText()));

    var sourceBranch = "file1.txt";
    var targetBranch = "file2.txt";
    var left = getPluginManagementService().getDataRequestService().getFileOfBranch(sourceBranch);
    var right = getPluginManagementService().getDataRequestService().getFileOfBranch(targetBranch);

//    var string = String.join("\n", splittedFile);

    var content1 = DiffContentFactory.getInstance().create(left);
    var content2 = DiffContentFactory.getInstance().create(right);
    var request = new SimpleDiffRequest(
        prName,
        content1,
        content2,
        sourceBranch,
        targetBranch
    );

    //request.putUserData(DataContextKey, mergeRequest);
    DiffManager.getInstance().showDiff(project, request);
  }

  private JComponent createListPanel(JComponent buttonToolBar, Tree tree) {

    var gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;
    gbc.weighty = 0;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;

    var panel = new JPanel(new GridBagLayout());
    panel.add(buttonToolBar, gbc);

    gbc.weighty = 1;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridheight = 100;
    gbc.fill = GridBagConstraints.BOTH;


    panel.add(tree, gbc);

    panel.setPreferredSize(new Dimension(0, 200));

    return panel;
  }

  private JComponent createButtonToolBar(JPanel parent) {
    var instance = ActionManager.getInstance();
    var actionGroup = (ActionGroup) instance.getAction("io.github.askmeagain.pullrequest.group.pullrequests");
    var toolbar = instance.createActionToolbar("macro_magic_group_history", actionGroup, true);
    toolbar.setTargetComponent(parent);
    return toolbar.getComponent();
  }

}