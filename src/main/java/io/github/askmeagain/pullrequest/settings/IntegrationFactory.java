package io.github.askmeagain.pullrequest.settings;

import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;

import javax.swing.*;

public interface IntegrationFactory {

  JPanel create();
  ConnectionConfig getConfig();
}
