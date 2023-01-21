package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.gui.dialogs.DiffEditor;

import java.util.Optional;

@Service
public final class EditorService {

  public static EditorService getInstance() {
    return ApplicationManager.getApplication().getService(EditorService.class);
  }

  private DiffEditor diffEditor;

  public Optional<DiffEditor> getDiffView() {
    return Optional.ofNullable(diffEditor);
  }

  public void register(DiffEditor v) {
    diffEditor = v;
  }
}
