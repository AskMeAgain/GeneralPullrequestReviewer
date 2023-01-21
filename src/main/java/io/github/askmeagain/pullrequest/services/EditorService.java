package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.gui.dialogs.DiffView;

import java.util.Optional;

@Service
public final class EditorService {

  public static EditorService getInstance() {
    return ApplicationManager.getApplication().getService(EditorService.class);
  }

  private DiffView diffView;

  public Optional<DiffView> getDiffView() {
    return Optional.ofNullable(diffView);
  }

  public void register(DiffView v) {
    diffView = v;
  }
}
