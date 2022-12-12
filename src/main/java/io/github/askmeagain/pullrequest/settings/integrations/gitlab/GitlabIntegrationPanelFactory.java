package io.github.askmeagain.pullrequest.settings.integrations.gitlab;

import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import io.github.askmeagain.pullrequest.services.PasswordService;
import io.github.askmeagain.pullrequest.settings.integrations.IntegrationFactory;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionListener;

@RequiredArgsConstructor
public class GitlabIntegrationPanelFactory implements IntegrationFactory {

  private final JBPasswordField gitlabApiToken = new JBPasswordField();
  private final JBTextField name = new JBTextField();
  private final JBTextField gitlabUrl = new JBTextField();
  private final JBTextField gitlabProjects = new JBTextField();
  private final JCheckBox legacyGitlab = new JCheckBox();
  private final JButton delete = new JButton("Delete Integration");
  private final ConnectionConfig connectionConfig;

  private final ActionListener onDelete;

  private final PasswordService passwordService = PasswordService.getInstance();

  public JPanel create() {
    name.setText(connectionConfig.getName());
    gitlabUrl.setText(connectionConfig.getConfigs().get("gitlabUrl"));
    gitlabProjects.setText(connectionConfig.getConfigs().get("projects"));
    legacyGitlab.setSelected(Boolean.parseBoolean(connectionConfig.getConfigs().get("legacy_gitlab")));
    gitlabApiToken.setText(passwordService.getPassword(connectionConfig.getName()));

    delete.addActionListener(onDelete);

    return FormBuilder.createFormBuilder()
        .addLabeledComponent("Name", name, 1, false)
        .addLabeledComponent("Gitlab Api token", gitlabApiToken, 1, false)
        .addLabeledComponent("Gitlab url", gitlabUrl, 1, false)
        .addLabeledComponent("Projects", gitlabProjects, 1, false)
        .addLabeledComponent("Legacy gitlab", legacyGitlab, 1, false)
        .addComponent(delete)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
  }

  public String getPassword(){
    return String.valueOf(gitlabApiToken.getPassword());
  }

  public ConnectionConfig getConfig() {
    var config = new ConnectionConfig();
    config.setName(name.getText());
    config.setVcsImplementation(VcsImplementation.GITLAB);
    config.getConfigs().put("gitlabUrl", gitlabUrl.getText());
    config.getConfigs().put("legacy_gitlab", String.valueOf(legacyGitlab.isSelected()));
    config.getConfigs().put("projects", gitlabProjects.getText());

    return config;
  }
}
