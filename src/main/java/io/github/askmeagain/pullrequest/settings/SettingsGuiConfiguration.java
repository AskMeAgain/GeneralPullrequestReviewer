package io.github.askmeagain.pullrequest.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.util.Producer;
import io.github.askmeagain.pullrequest.services.StateService;
import lombok.Getter;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class SettingsGuiConfiguration implements Configurable {

  @Getter(lazy = true)
  private final StateService stateService = StateService.getInstance();
  private PullrequestSettingsWindow settingsComponent;

  @Override
  @Nls(capitalization = Nls.Capitalization.Title)
  public String getDisplayName() {
    return "Pullrequest Settings";
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return settingsComponent.getPreferredFocusedComponent();
  }

  @Override
  public JComponent createComponent() {
    var state = getStateService().getState();

    settingsComponent = new PullrequestSettingsWindow(state.getMap());

    return settingsComponent.getPreferredFocusedComponent();
  }

  @Override
  public boolean isModified() {
    //TODO
    return true;
  }

  @Override
  public void apply() {
    var state = getStateService().getState();

    var map = settingsComponent.getConnectionConfigs()
        .stream()
        .peek(connectionConfig -> connectionConfig.getRefresh().run())
        .collect(Collectors.toList());

    state.setConnectionConfigs(map);
  }

  @Override
  public void reset() {
    //nothing to do. This is a bug, since this is getting executed multiple times for no reason
  }

  @Override
  public void disposeUIResources() {
    settingsComponent = null;
  }
}