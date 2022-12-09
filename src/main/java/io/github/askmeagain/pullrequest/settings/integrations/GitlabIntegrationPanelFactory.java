package io.github.askmeagain.pullrequest.settings.integrations;

import com.intellij.ui.TabbedPane;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import io.github.askmeagain.pullrequest.services.StateService;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.util.List;

@RequiredArgsConstructor
public class GitlabIntegrationPanelFactory implements IntegrationFactory {

  private final JBPasswordField gitlabApiToken = new JBPasswordField();
  private final JBTextField name = new JBTextField();
  private final JBTextField gitlabUrl = new JBTextField();
  private final JBTextField gitlabProjects = new JBTextField();
  private final JButton delete = new JButton("Delete Integration");
  private final int index;
  private final JBTabbedPane tabbedPane;
  private final List<ConnectionConfig> list;

  public JPanel create() {

    var connectionConfig = list.get(index);

    name.setText(connectionConfig.getName());
    gitlabUrl.setText(connectionConfig.getConfigs().get("gitlabUrl"));
    gitlabProjects.setText(connectionConfig.getConfigs().get("projects"));
    gitlabApiToken.setText(connectionConfig.getConfigs().get("token"));

    delete.addActionListener(l -> {
      list.remove(index);
      tabbedPane.remove(index);
    });

    return FormBuilder.createFormBuilder()
        .addLabeledComponent("Name", name, 1, false)
        .addLabeledComponent("Gitlab Api token", gitlabApiToken, 1, false)
        .addLabeledComponent("Gitlab url", gitlabUrl, 1, false)
        .addLabeledComponent("Projects", gitlabProjects, 1, false)
        .addComponent(delete)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
  }

  public ConnectionConfig getConfig() {
    var connectionConfig = list.get(index);

    connectionConfig.setRefresh(() -> {
      connectionConfig.setVcsImplementation(VcsImplementation.GITLAB);
      connectionConfig.setName(name.getText());
      connectionConfig.getConfigs().put("token", new String(gitlabApiToken.getPassword()));
      connectionConfig.getConfigs().put("gitlabUrl", gitlabUrl.getText());
      connectionConfig.getConfigs().put("projects", gitlabProjects.getText());
    });

    connectionConfig.getRefresh().run();

    return connectionConfig;
  }
}
