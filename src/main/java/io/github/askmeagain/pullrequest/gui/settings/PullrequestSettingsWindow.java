package io.github.askmeagain.pullrequest.gui.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import io.github.askmeagain.pullrequest.services.StateService;
import io.github.askmeagain.pullrequest.settings.integrations.IntegrationFactory;
import io.github.askmeagain.pullrequest.settings.integrations.gitlab.GitlabIntegrationPanelFactory;
import io.github.askmeagain.pullrequest.settings.integrations.test.TestIntegrationPanelFactory;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PullrequestSettingsWindow {

  @Getter
  private final JPanel panel;
  private final JComboBox<VcsImplementation> selectedVcsImplementation = new ComboBox<>(getIntegrations());
  private final JBTabbedPane tabbedPane = new JBTabbedPane();

  @Getter
  private final List<IntegrationFactory> integrationPanels = new ArrayList<>();
  @Getter
  private final ColorPanel mergeRequestColor = new ColorPanel();
  @Getter
  private final ColorPanel fileColor = new ColorPanel();
  @Getter
  private final ColorPanel mergeRequestHintsInDiffView = new ColorPanel();

  private static VcsImplementation[] getIntegrations() {
    var integrations = new ArrayList<>(List.of(VcsImplementation.GITLAB));

    if (ApplicationManager.getApplication().isInternal()) {
      integrations.add(VcsImplementation.TEST);
    }

    return integrations.toArray(VcsImplementation[]::new);
  }

  public PullrequestSettingsWindow(Map<String, ConnectionConfig> configMap) {
    var addProjectButton = new JButton("Add Connection");
    addProjectButton.addActionListener(a -> {
      var selectedItem = (VcsImplementation) selectedVcsImplementation.getSelectedItem();
      var testConnectionPanel = resolveComponent(new ConnectionConfig(selectedItem), tabbedPane.getSelectedIndex(), tabbedPane);
      var component = testConnectionPanel.create();
      integrationPanels.add(testConnectionPanel);
      tabbedPane.insertTab("New Test Connection", null, component, "", tabbedPane.getSelectedIndex());
      tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
    });

    var connectionConfigs = new ArrayList<>(configMap.values());
    for (int i = 0; i < connectionConfigs.size(); i++) {
      var connection = connectionConfigs.get(i);
      var impl = resolveComponent(connection, i, tabbedPane);
      integrationPanels.add(impl);
      tabbedPane.addTab(connection.getName(), impl.create());
    }

    tabbedPane.addTab("Add Connection", FormBuilder.createFormBuilder()
        .addLabeledComponent("Select vcs integration", selectedVcsImplementation)
        .addComponent(addProjectButton)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel());

    panel = FormBuilder.createFormBuilder()
        .addComponent(tabbedPane)
        .addSeparator()
        .addComponent(colorPickers())
        .addComponentFillVertically(new JPanel(), 10)
        .getPanel();

    setSelectedTab(StateService.getInstance().getSelectedTab());
  }

  private void setSelectedTab(String connectionName) {
    for (int i = 0; i < integrationPanels.size(); i++) {
      if (integrationPanels.get(i).getConfig().getName().equals(connectionName)) {
        tabbedPane.setSelectedIndex(i);
      }
    }
  }

  private IntegrationFactory resolveComponent(ConnectionConfig connectionConfig, int index, JBTabbedPane tabbedPane) {
    switch (connectionConfig.getVcsImplementation()) {
      case GITLAB:
        return new GitlabIntegrationPanelFactory(connectionConfig, deleteListener(index, tabbedPane));
      case TEST:
        return new TestIntegrationPanelFactory(connectionConfig, deleteListener(index, tabbedPane));
    }
    throw new RuntimeException("This will not happen");
  }

  private ActionListener deleteListener(int index, JBTabbedPane tabbedPane) {
    return l -> {
      tabbedPane.remove(index);
      integrationPanels.remove(index);
    };
  }

  private JPanel colorPickers() {
    return FormBuilder.createFormBuilder()
        .addLabeledComponent("MergeRequests", mergeRequestColor)
        .addLabeledComponent("Files", fileColor)
        .addLabeledComponent("MergeRequest comment hints", mergeRequestHintsInDiffView)
        .addComponentFillVertically(new JPanel(), 10)
        .getPanel();
  }
}