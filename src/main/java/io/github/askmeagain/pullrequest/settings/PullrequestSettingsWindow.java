package io.github.askmeagain.pullrequest.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import io.github.askmeagain.pullrequest.settings.integrations.GitlabIntegrationPanelFactory;
import io.github.askmeagain.pullrequest.settings.integrations.IntegrationFactory;
import lombok.Getter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PullrequestSettingsWindow {

  private final JPanel panel;

  private final JComboBox<VcsImplementation> selectedVcsImplementation = new ComboBox<>(getIntegrations());

  private static VcsImplementation[] getIntegrations() {
    var integrations = new ArrayList<>(List.of(VcsImplementation.GITLAB));

    if (ApplicationManager.getApplication().isInternal()) {
      integrations.add(VcsImplementation.TEST);
    }

    return integrations.toArray(VcsImplementation[]::new);
  }

  @Getter
  private final List<IntegrationFactory> integrationPanels = new ArrayList<>();
  @Getter
  private final ColorPanel mergeRequestColor = new ColorPanel();
  @Getter
  private final ColorPanel fileColor = new ColorPanel();
  @Getter
  private final ColorPanel mergeRequestHintsInDiffView = new ColorPanel();

  public PullrequestSettingsWindow(Map<String, ConnectionConfig> configMap) {
    var tabbedPane = new JBTabbedPane();
    var connectionConfigs = new ArrayList<>(configMap.values());

    var addProjectButton = new JButton("Add Project");
    addProjectButton.addActionListener(a -> {
      if (selectedVcsImplementation.getSelectedItem() == VcsImplementation.GITLAB) {
        var gitlabConnectionPanel = new GitlabIntegrationPanelFactory(
            new ConnectionConfig(),
            l -> {
              tabbedPane.remove(tabbedPane.getSelectedIndex());
              integrationPanels.remove(tabbedPane.getSelectedIndex());
            }
        );
        var component = gitlabConnectionPanel.create();
        integrationPanels.add(gitlabConnectionPanel);
        tabbedPane.insertTab("New Gitlab Connection", null, component, "", tabbedPane.getSelectedIndex());
        tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
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
      var impl = resolveComponent(connection, i, tabbedPane);
      integrationPanels.add(impl);
      var component = impl.create();
      tabbedPane.addTab(connection.getName(), component);
    }

    tabbedPane.addTab("Add Connection", addTab);

    panel = FormBuilder.createFormBuilder()
        .addComponent(tabbedPane)
        .addSeparator()
        .addComponent(colorPickers())
        .addComponentFillVertically(new JPanel(), 10)
        .getPanel();
  }

  private IntegrationFactory resolveComponent(ConnectionConfig connectionConfig, int index, JBTabbedPane tabbedPane) {
    if (connectionConfig.getVcsImplementation() == VcsImplementation.GITLAB) {
      return new GitlabIntegrationPanelFactory(connectionConfig, l -> {
        tabbedPane.remove(index);
        integrationPanels.remove(index);
      });
    }
    throw new RuntimeException("whatever");
  }

  private JPanel colorPickers() {
    return FormBuilder.createFormBuilder()
        .addLabeledComponent("MergeRequests", mergeRequestColor)
        .addLabeledComponent("Files", fileColor)
        .addLabeledComponent("MergeRequest comment hints", mergeRequestHintsInDiffView)
        .addComponentFillVertically(new JPanel(), 10)
        .getPanel();
  }

  public JComponent getPreferredFocusedComponent() {
    return panel;
  }
}