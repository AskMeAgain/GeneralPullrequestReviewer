package io.github.askmeagain.pullrequest.extensions;

import com.intellij.diff.DiffContext;
import com.intellij.diff.DiffExtension;
import com.intellij.diff.FrameDiffTool;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.tools.simple.SimpleDiffViewer;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.Key;
import com.intellij.ui.JBColor;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.gui.OnHoverOverCommentListener;
import io.github.askmeagain.pullrequest.gui.MouseClickListener;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class DiffWindowExtension extends DiffExtension {

  private static final TextAttributes EDITOR_ATTRIBUTES = new TextAttributes(JBColor.red, null, null, null, Font.BOLD);
  @Getter(lazy = true)
  private final PluginManagementService pluginManagementService = PluginManagementService.getInstance();

  @Override
  public void onViewerCreated(FrameDiffTool.@NotNull DiffViewer viewer, @NotNull DiffContext context, @NotNull DiffRequest request) {
    var left = ((SimpleDiffViewer) viewer).getEditor1();
    var right = ((SimpleDiffViewer) viewer).getEditor2();

    left.putUserData(MouseClickListener.IsSource, true);
    right.putUserData(MouseClickListener.IsSource, false);

    left.putUserData(MouseClickListener.MergeRequestId, request.getUserData(MouseClickListener.MergeRequestId));
    right.putUserData(MouseClickListener.MergeRequestId, request.getUserData(MouseClickListener.MergeRequestId));

    left.putUserData(MouseClickListener.FileName, request.getUserData(MouseClickListener.FileName));
    right.putUserData(MouseClickListener.FileName, request.getUserData(MouseClickListener.FileName));

    var listener = new OnHoverOverCommentListener(request.getUserData(MouseClickListener.AllDiscussions));

    doForEditor(left, request, MouseClickListener.DataContextKeyTarget, listener);
    doForEditor(right, request, MouseClickListener.DataContextKeySource, listener);
  }

  @SneakyThrows
  private void doForEditor(EditorEx editor, DiffRequest request, Key<ReviewFile> fileKey, OnHoverOverCommentListener listener) {
    applyColorScheme(editor);

    var textAttributes = new TextAttributes(null, JBColor.green, null, null, Font.PLAIN);

    var reviewDiscussion = new ArrayList<>(request.getUserData(fileKey).getReviewDiscussions());

    for (var discussion : reviewDiscussion) {
      var line = discussion.getLine();
      var startOffset = editor.getDocument().getLineStartOffset(line);
      var endOffset = editor.getDocument().getLineEndOffset(line);
      var markupModel = editor.getMarkupModel();

      markupModel.addRangeHighlighter(startOffset, endOffset, HighlighterLayer.SELECTION, textAttributes, HighlighterTargetArea.EXACT_RANGE);
    }
    editor.addEditorMouseMotionListener(listener);
    editor.addEditorMouseListener(listener);
  }

  private static void applyColorScheme(EditorEx editor) {
    var colorsScheme = editor.getColorsScheme();
    colorsScheme.setAttributes(EditorColors.FOLDED_TEXT_ATTRIBUTES, EDITOR_ATTRIBUTES);
    editor.setColorsScheme(colorsScheme);
  }
}
