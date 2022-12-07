package io.github.askmeagain.pullrequest.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import lombok.Getter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PullrequestSettingsWindow {

  private final JBTabbedPane tabbedPane;

  private final JComboBox<VcsImplementation> selectedVcsImplementation = new ComboBox<>(new VcsImplementation[]{
      VcsImplementation.GITLAB
  });

  @Getter
  private final List<ConnectionConfig> connectionConfigs;

  public PullrequestSettingsWindow(Map<String, ConnectionConfig> abc) {
    this.connectionConfigs = new ArrayList<>(abc.values());
    tabbedPane = new JBTabbedPane();

    var addProjectButton = new JButton("Add Project");
    addProjectButton.addActionListener(a -> {
      if (selectedVcsImplementation.getSelectedItem() == VcsImplementation.GITLAB) {
        var gitlabConnectionPanel = new GitlabConnectionPanel(new ConnectionConfig("New Gitlab Connection"));
        var component = gitlabConnectionPanel.create();
        this.connectionConfigs.add(gitlabConnectionPanel.getConfig());
        tabbedPane.insertTab("New Gitlab Connection", null, component, "", tabbedPane.getSelectedIndex());
      } else {
        System.out.println("Not implemented");
      }
    });

    var addTab = FormBuilder.createFormBuilder()
        .addLabeledComponent("Select vcs integration", selectedVcsImplementation)
        .addComponent(addProjectButton)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    for (int i = 0; i < connectionConfigs.size(); i++) {
      var connection = connectionConfigs.get(i);
      var impl = resolveComponent(connection);
      var component = impl.create();
      connectionConfigs.set(i, impl.getConfig());
      tabbedPane.addTab(connection.getName(), component);
    }

    tabbedPane.addTab("Add Connection", addTab);
  }

  private PanelImpl resolveComponent(ConnectionConfig connectionConfig) {
    if (connectionConfig.getVcsImplementation() == VcsImplementation.GITLAB) {
      return new GitlabConnectionPanel(connectionConfig);
    }
    throw new RuntimeException("whatever");
  }

  public JComponent getPreferredFocusedComponent() {
    return tabbedPane;
  }
}