package io.github.askmeagain.pullrequest.gui.toolwindow;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.listener.MouseClickListener;
import io.github.askmeagain.pullrequest.listener.PluginTreeExpansionListener;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PullrequestToolWindow implements ToolWindowFactory, DumbAware {

  @Getter(lazy = true)
  private final PluginManagementService pluginManagementService = PluginManagementService.getInstance();

  @Override
  public void createToolWindowContent(@NotNull Project project, ToolWindow toolWindow) {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1;
    gbc.weighty = 0.5;

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;

    var panel = new JPanel(new GridBagLayout());
    panel.add(createPanel(panel), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;

    var content = ApplicationManager.getApplication()
        .getService(ContentFactory.class)
        .createContent(panel, "", false);
    var contentManager = toolWindow.getContentManager();

    contentManager.addContent(content);
  }

  private JComponent createPanel(JPanel parent) {
    var buttonToolBar = createButtonToolBar(parent);

    var tree = getPluginManagementService().getTree();
    tree.setRootVisible(false);

    tree.addMouseListener(new MouseClickListener(tree));
    tree.addTreeWillExpandListener(new PluginTreeExpansionListener());

    return createListPanel(buttonToolBar, tree);
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