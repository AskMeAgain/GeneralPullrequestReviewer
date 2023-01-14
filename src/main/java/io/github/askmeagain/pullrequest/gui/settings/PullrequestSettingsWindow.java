package io.github.askmeagain.pullrequest.gui.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColorPanel;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import io.github.askmeagain.pullrequest.services.StateService;
import io.github.askmeagain.pullrequest.settings.IntegrationFactory;
import io.github.askmeagain.pullrequest.settings.github.GithubIntegrationPanelFactory;
import io.github.askmeagain.pullrequest.settings.gitlab.GitlabIntegrationPanelFactory;
import io.github.askmeagain.pullrequest.settings.test.TestIntegrationPanelFactory;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    var integrations = new ArrayList<>(List.of(VcsImplementation.GITLAB, VcsImplementation.GITHUB));

    if (ApplicationManager.getApplication().isInternal()) {
      integrations.add(VcsImplementation.TEST);
    }

    return integrations.toArray(VcsImplementation[]::new);
  }

  public PullrequestSettingsWindow(Map<String, ConnectionConfig> configMap) {
    var addProjectButton = new JButton("Add Connection");
    addProjectButton.addActionListener(a -> {
      var selectedItem = (VcsImplementation) selectedVcsImplementation.getSelectedItem();
      var testConnectionPanel = resolveComponent(new ConnectionConfig(selectedItem));
      var component = testConnectionPanel.create();
      integrationPanels.add(testConnectionPanel);
      tabbedPane.insertTab("New Test Connection", null, component, "", tabbedPane.getSelectedIndex());
      tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
    });

    for (var connection : configMap.values()) {
      var impl = resolveComponent(connection);
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

  private IntegrationFactory resolveComponent(ConnectionConfig connectionConfig) {
    switch (connectionConfig.getVcsImplementation()) {
      case GITLAB:
        return new GitlabIntegrationPanelFactory(connectionConfig, deleteListener(connectionConfig));
      case TEST:
        return new TestIntegrationPanelFactory(connectionConfig, deleteListener(connectionConfig));
      case GITHUB:
        return new GithubIntegrationPanelFactory(connectionConfig, deleteListener(connectionConfig));
    }
    throw new RuntimeException("This will not happen");
  }

  private ActionListener deleteListener(ConnectionConfig connection) {
    return unused -> {
      integrationPanels.removeIf(x -> x.getConfig().getName().equals(connection.getName()));

      for (int i = 0; i < tabbedPane.getTabCount(); i++) {
        var componentAt = tabbedPane.getComponentAt(i);
        if (Objects.equals(componentAt.getName(), connection.getName())) {
          tabbedPane.remove(i);
          return;
        }
      }
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