package io.github.askmeagain.pullrequest.settings;

import javax.swing.*;

public interface PanelImpl {

  JPanel create();
  ConnectionConfig getConfig();
}
