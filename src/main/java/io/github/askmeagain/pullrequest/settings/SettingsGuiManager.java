package io.github.askmeagain.pullrequest.settings;

import com.intellij.openapi.options.Configurable;
import lombok.Getter;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import java.util.Objects;

public class SettingsGuiManager implements Configurable {

  @Getter(lazy = true)
  private final PersistenceManagementService persistenceManagementService = PersistenceManagementService.getInstance();
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
    var state = getPersistenceManagementService().getState();

    settingsComponent = new PullrequestSettingsWindow();

    settingsComponent.setGitlabApiToken(state.getGitlabToken());
    settingsComponent.setGitlabGroup(state.getGitlabGroupId());
    settingsComponent.setGitlabUrl(state.getGitlabUrl());
    settingsComponent.setGitlabProjects(state.getGitlabProjects());
    settingsComponent.setVcsImplementation(state.getSelectedVcsImplementation());

    return settingsComponent.getTabbedPane();
  }

  @Override
  public boolean isModified() {
    var state = getPersistenceManagementService().getState();

    var urlChanged = !Objects.equals(state.getGitlabUrl(), settingsComponent.getGitlabUrl());
    var gitlabTokenChanged = !Objects.equals(state.getGitlabToken(), settingsComponent.getGitlabApiToken());
    var selectedVcsIntegrationChanged = !Objects.equals(state.getSelectedVcsImplementation(), settingsComponent.getVcsImplementation());
    var groupChanged = !Objects.equals(state.getGitlabGroupId(), settingsComponent.getGitlabGroup());
    var gitlabProjectsChanged = !Objects.equals(state.getGitlabProjects(), settingsComponent.getGitlabProjects());

    return urlChanged || gitlabTokenChanged || selectedVcsIntegrationChanged || groupChanged || gitlabProjectsChanged;
  }

  @Override
  public void apply() {
    var state = getPersistenceManagementService().getState();
    state.setGitlabUrl(settingsComponent.getGitlabUrl());
    state.setGitlabToken(settingsComponent.getGitlabApiToken());
    state.setSelectedVcsImplementation(settingsComponent.getVcsImplementation());
    state.setGitlabGroupId(settingsComponent.getGitlabGroup());
    state.setGitlabProjects(settingsComponent.getGitlabProjects());
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