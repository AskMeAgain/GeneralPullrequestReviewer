package io.github.askmeagain.pullrequest.settings.integrations.test;

import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import io.github.askmeagain.pullrequest.settings.integrations.IntegrationFactory;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionListener;

@RequiredArgsConstructor
public class TestIntegrationPanelFactory implements IntegrationFactory {

  private final JBTextField name = new JBTextField();
  private final JButton delete = new JButton("Delete Integration");
  private final ConnectionConfig connectionConfig;
  private final ActionListener onDelete;

  public JPanel create() {
    name.setText(connectionConfig.getName());

    delete.addActionListener(onDelete);

    return FormBuilder.createFormBuilder()
        .addLabeledComponent("Name", name, 1, false)
        .addComponent(delete)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
  }

  public String getPassword() {
    return "-";
  }

  public ConnectionConfig getConfig() {
    var config = new ConnectionConfig();
    config.setName(name.getText());
    config.setVcsImplementation(VcsImplementation.TEST);
    return config;
  }
}
