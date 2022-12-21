package io.github.askmeagain.pullrequest.settings.integrations;

import com.intellij.icons.AllIcons;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public interface IntegrationFactory {

  JPanel create();

  ConnectionConfig getConfig();

  String getPassword();

  @NotNull
  default JLabel getHelpLabel(String label, String helpText) {
    var projectsLabel = new JLabel(label);
    projectsLabel.setIcon(AllIcons.General.ContextHelp);
    projectsLabel.setToolTipText(helpText);
    projectsLabel.setHorizontalTextPosition(SwingConstants.LEFT);
    return projectsLabel;
  }
}
