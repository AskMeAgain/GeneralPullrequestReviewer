package io.github.askmeagain.pullrequest.settings.github;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import io.github.askmeagain.pullrequest.services.PasswordService;
import io.github.askmeagain.pullrequest.services.vcs.github.GithubService;
import io.github.askmeagain.pullrequest.settings.IntegrationFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionListener;

@RequiredArgsConstructor
public class GithubIntegrationPanelFactory implements IntegrationFactory {

  private final JBPasswordField githubApiToken = new JBPasswordField();
  private final JBTextField name = new JBTextField();
  private final JBTextField githubUrl = new JBTextField();
  private final JBTextField repoName = new JBTextField();
  private final JBTextField userName = new JBTextField();
  private final JButton delete = new JButton("Delete Connection");
  private final JButton test = new JButton("Test");

  private final ConnectionConfig connectionConfig;

  private final ActionListener onDelete;

  private final PasswordService passwordService = PasswordService.getInstance();
  private final GithubService githubService = ApplicationManager.getApplication().getService(GithubService.class);


  public JPanel create() {
    name.setText(connectionConfig.getName());
    githubUrl.setText(connectionConfig.getConfigs().get("githubUrl"));

    if (StringUtils.isBlank(connectionConfig.getConfigs().get("githubUrl"))) {
      githubUrl.setText("https://api.github.com/repos/");
    }

    repoName.setText(connectionConfig.getConfigs().get("repoName"));
    userName.setText(connectionConfig.getConfigs().get("userName"));

    githubApiToken.setText(passwordService.getPassword(connectionConfig.getName()));

    delete.addActionListener(onDelete);
    test.addActionListener(l -> {
      try {
        //TODO
        //githubService.ping(getConfig(), gitlabProjects.getText().split(",")[0], new String(gitlabApiToken.getPassword()));
        JOptionPane.showMessageDialog(null, "Successful");
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Could not connect");
      }
    });

    var repoLabel = getHelpLabel("Repo Name", "Comma separated list of repo names of the same user");
    var usernameLabel = getHelpLabel("User Name", "Username in the repo url");
    var urlLabel = getHelpLabel("Github url", "api url should end with api/v4/");

    var panel = FormBuilder.createFormBuilder()
        .addLabeledComponent("Name", name, 1, false)
        .addLabeledComponent("Github api token", githubApiToken, 1, false)
        .addLabeledComponent(urlLabel, githubUrl, 1, false)
        .addLabeledComponent(usernameLabel, userName, 1, false)
        .addLabeledComponent(repoLabel, repoName, 1, false)
        .addComponent(delete)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
    panel.setName(connectionConfig.getName());
    return panel;
  }

  public String getPassword() {
    return String.valueOf(githubApiToken.getPassword());
  }

  public ConnectionConfig getConfig() {
    var config = new ConnectionConfig();
    config.setName(name.getText());
    config.setVcsImplementation(VcsImplementation.GITHUB);
    config.getConfigs().put("githubUrl", githubUrl.getText());
    config.getConfigs().put("userName", userName.getText());
    config.getConfigs().put("repoName", repoName.getText());

    return config;
  }
}
