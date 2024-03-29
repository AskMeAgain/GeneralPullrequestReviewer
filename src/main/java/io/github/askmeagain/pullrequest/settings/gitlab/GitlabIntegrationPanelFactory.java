package io.github.askmeagain.pullrequest.settings.gitlab;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import io.github.askmeagain.pullrequest.services.PasswordService;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import io.github.askmeagain.pullrequest.settings.IntegrationFactory;
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
  private final JButton delete = new JButton("Delete Connection");
  private final JButton test = new JButton("Test");
  private final ConnectionConfig connectionConfig;
  private final GitlabService gitlabService = ApplicationManager.getApplication().getService(GitlabService.class);

  private final ActionListener onDelete;

  private final PasswordService passwordService = PasswordService.getInstance();

  public JPanel create() {
    name.setText(connectionConfig.getName());
    gitlabUrl.setText(connectionConfig.getConfigs().get("gitlabUrl"));
    gitlabProjects.setText(connectionConfig.getConfigs().get("projects"));
    legacyGitlab.setSelected(Boolean.parseBoolean(connectionConfig.getConfigs().get("legacy_gitlab")));
    gitlabApiToken.setText(passwordService.getPassword(connectionConfig.getName()));

    test.addActionListener(l -> {
      try {
        gitlabService.ping(getConfig(), gitlabProjects.getText().split(",")[0], new String(gitlabApiToken.getPassword()));
        JOptionPane.showMessageDialog(null, "Successful");
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Could not connect");
      }
    });
    delete.addActionListener(onDelete);

    var projectsLabel = getHelpLabel("Projects", "Comma separated list of projects");
    var urlLabel = getHelpLabel("Gitlab url", "api url should end with api/v4/");

    var panel = FormBuilder.createFormBuilder()
        .addLabeledComponent("Name", name, 1, false)
        .addLabeledComponent("Gitlab api token", gitlabApiToken, 1, false)
        .addLabeledComponent(urlLabel, gitlabUrl, 1, false)
        .addLabeledComponent(projectsLabel, gitlabProjects, 1, false)
        .addLabeledComponent("Legacy gitlab", legacyGitlab, 1, false)
        .addLabeledComponent(test, delete)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
    panel.setName(connectionConfig.getName());
    return panel;
  }

  public String getPassword() {
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
