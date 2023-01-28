package io.github.askmeagain.pullrequest.extensions;

import com.intellij.diff.DiffContext;
import com.intellij.diff.DiffExtension;
import com.intellij.diff.FrameDiffTool;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.tools.simple.SimpleDiffViewer;
import io.github.askmeagain.pullrequest.gui.dialogs.DiffEditor;
import io.github.askmeagain.pullrequest.services.EditorService;
import org.jetbrains.annotations.NotNull;

public class DiffWindowExtension extends DiffExtension {

  @Override
  public void onViewerCreated(FrameDiffTool.@NotNull DiffViewer viewer, @NotNull DiffContext context, @NotNull DiffRequest request) {
    var targetEditor = ((SimpleDiffViewer) viewer).getEditor1();
    var sourceEditor = ((SimpleDiffViewer) viewer).getEditor2();

    var diffViewer = new DiffEditor(request, sourceEditor, targetEditor);

    EditorService.getInstance().register(diffViewer);
  }
}
