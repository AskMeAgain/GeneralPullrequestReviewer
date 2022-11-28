package io.github.askmeagain.pullrequest;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffDialogHints;
import com.intellij.diff.DiffManager;
import com.intellij.diff.contents.DocumentContent;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.WindowWrapper;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.content.ContentFactory;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PullrequestToolWindow implements ToolWindowFactory, DumbAware {

  @Getter(lazy = true)
  private final PullrequestService pullrequestService = PullrequestService.getInstance();

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
    panel.add(createHistoryPanel(panel, project), gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;

    var content = ApplicationManager.getApplication()
        .getService(ContentFactory.class)
        .createContent(panel, "", false);
    var contentManager = toolWindow.getContentManager();

    contentManager.addContent(content);
  }

  private JComponent createHistoryPanel(JPanel parent, Project project) {
    var buttonToolBar = createButtonToolBar("history", parent);

    var theRealList = getPullrequestService().getDefaultListModelString();

    var jList = new JBList<>(theRealList);

    jList.addMouseListener((new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        var index = list.getSelectedIndex();

        var string = theRealList.get(index);

        DocumentContent content1 = DiffContentFactory.getInstance().create(string);
        DocumentContent content2 = DiffContentFactory.getInstance().create(string + " DIFF!!!!");
        SimpleDiffRequest request = new SimpleDiffRequest("Window Title", content1, content2, "Title 1", "Title 2");
        request.putUserData(Key.create("test123"), index);
        DiffManager.getInstance().showDiff(project, request);
      }
    }));



    return createListPanel(buttonToolBar, jList);
  }

  private JComponent createListPanel(JComponent buttonToolBar, JBList<?> anActionJbList) {

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

    var pane = new JBScrollPane(anActionJbList);
    panel.add(pane, gbc);

    panel.setPreferredSize(new Dimension(0, 200));

    return panel;
  }

  private JComponent createButtonToolBar(String groupName, JPanel parent) {

    var instance = ActionManager.getInstance();
    var actionGroup = (ActionGroup) instance.getAction("io.github.askmeagain.pullrequest.group.pullrequests");
    var toolbar = instance.createActionToolbar("macro_magic_group_" + groupName, actionGroup, true);
    toolbar.setTargetComponent(parent);
    return toolbar.getComponent();
  }

}