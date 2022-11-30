package io.github.askmeagain.pullrequest.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import org.jetbrains.annotations.NotNull;

@Service
@State(name = "PullrequestPlugin", storages = @Storage("pullrequest-plugin.xml"))
public final class PersistenceManagementService implements PersistentStateComponent<PullrequestPluginState> {

  private PullrequestPluginState pullrequestPluginState = new PullrequestPluginState();

  @Override
  public @NotNull PullrequestPluginState getState() {
    return pullrequestPluginState;
  }

  @Override
  public void loadState(@NotNull PullrequestPluginState state) {
    pullrequestPluginState = state;
  }

  public static PersistenceManagementService getInstance() {
    return ApplicationManager.getApplication().getService(PersistenceManagementService.class);
  }
}