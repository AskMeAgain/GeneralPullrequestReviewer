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
import java.awt.event.ActionListener;
import java.util.List;

@RequiredArgsConstructor
public class GitlabIntegrationPanelFactory implements IntegrationFactory {

  private final JBPasswordField gitlabApiToken = new JBPasswordField();
  private final JBTextField name = new JBTextField();
  private final JBTextField gitlabUrl = new JBTextField();
  private final JBTextField gitlabProjects = new JBTextField();
  private final JButton delete = new JButton("Delete Integration");
  private final ConnectionConfig connectionConfig;

  private final ActionListener onDelete;

  public JPanel create() {
    name.setText(connectionConfig.getName());
    gitlabUrl.setText(connectionConfig.getConfigs().get("gitlabUrl"));
    gitlabProjects.setText(connectionConfig.getConfigs().get("projects"));
    gitlabApiToken.setText(connectionConfig.getPassword());

    delete.addActionListener(onDelete);

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

    var config = new ConnectionConfig();
    config.setPassword(String.valueOf(gitlabApiToken.getPassword()));
    config.setName(name.getText());
    config.setVcsImplementation(VcsImplementation.GITLAB);
    config.getConfigs().put("gitlabUrl", gitlabUrl.getText());
    config.getConfigs().put("projects", gitlabProjects.getText());

    return config;
  }
}
