package io.github.askmeagain.pullrequest.windows;

import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.contents.DocumentContent;
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
import io.github.askmeagain.pullrequest.dto.MergeRequest;
import io.github.askmeagain.pullrequest.services.PluginStateService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class PullrequestToolWindow implements ToolWindowFactory, DumbAware {

  public static final @NotNull Key<MergeRequest> DataContextKey = Key.create("selectedFileIndex");

  @Getter(lazy = true)
  private final PluginStateService pluginStateService = PluginStateService.getInstance();

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

    var theRealList = getPluginStateService().getDefaultListModelString();

    var jList = new JBList<>(theRealList);

    jList.addMouseListener((new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        var list = (JList<MergeRequest>) evt.getSource();
        var index = list.getSelectedIndex();

        var mergeRequest = theRealList.get(index);
        var reviewFile = mergeRequest.getFiles().get(0);
        var splittedFile = new ArrayList<>(List.of(reviewFile.getFileContent().split("\n")));

        reviewFile.getReviewComments().stream()
            .sorted((l, r) -> -1 * Integer.compare(l.getLine(), r.getLine()))
            .forEach(comment -> splittedFile.add(comment.getLine(), comment.getText()));

        var string = String.join("\n", splittedFile);

        var content1 = DiffContentFactory.getInstance().create(string);
        var content2 = DiffContentFactory.getInstance().create(string);
        var request = new SimpleDiffRequest(
            mergeRequest.getName(),
            content1,
            content2,
            mergeRequest.getSourceBranch(),
            mergeRequest.getTargetBranch()
        );
        request.putUserData(DataContextKey, mergeRequest);
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

  private JComponent createButtonToolBar(JPanel parent) {
    var instance = ActionManager.getInstance();
    var actionGroup = (ActionGroup) instance.getAction("io.github.askmeagain.pullrequest.group.pullrequests");
    var toolbar = instance.createActionToolbar("macro_magic_group_history", actionGroup, true);
    toolbar.setTargetComponent(parent);
    return toolbar.getComponent();
  }

}