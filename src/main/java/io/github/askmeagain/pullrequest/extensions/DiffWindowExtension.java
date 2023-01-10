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
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.listener.OnHoverOverCommentListener;
import io.github.askmeagain.pullrequest.services.StateService;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DiffWindowExtension extends DiffExtension {

  private static final TextAttributes EDITOR_ATTRIBUTES = new TextAttributes(JBColor.red, null, null, null, Font.BOLD);

  private final PullrequestPluginState state = StateService.getInstance().getState();

  private final List<Key> keys = List.of(
      TransferKey.ProjectId,
      TransferKey.CommitId,
      TransferKey.Connection,
      TransferKey.FileName,
      TransferKey.MergeRequestId,
      TransferKey.FileHunk
  );

  @Override
  public void onViewerCreated(FrameDiffTool.@NotNull DiffViewer viewer, @NotNull DiffContext context, @NotNull DiffRequest request) {
    var sourceEditor = ((SimpleDiffViewer) viewer).getEditor1();
    var targetEditor = ((SimpleDiffViewer) viewer).getEditor2();

    sourceEditor.putUserData(TransferKey.IsSource, true);
    targetEditor.putUserData(TransferKey.IsSource, false);

    var listener = new OnHoverOverCommentListener(
        request.getUserData(TransferKey.AllDiscussions),
        request.getUserData(TransferKey.Connection)
    );

    doForEditor(targetEditor, request, TransferKey.DataContextKeyTarget, listener);
    doForEditor(sourceEditor, request, TransferKey.DataContextKeySource, listener);
  }

  @SneakyThrows
  private void doForEditor(EditorEx editor, DiffRequest request, Key<ReviewFile> fileKey, OnHoverOverCommentListener listener) {
    applyColorScheme(editor);

    for (var key : keys) {
      transferData(editor, request, key);
    }

    var textAttributes = new TextAttributes(null, Color.decode(state.getDiscussionTextColor()), null, null, Font.PLAIN);

    var reviewDiscussion = new ArrayList<>(request.getUserData(fileKey).getReviewDiscussions());

    for (var discussion : reviewDiscussion) {
      var startOffset = editor.getDocument().getLineStartOffset(discussion.getStartLine());
      var endOffset = editor.getDocument().getLineEndOffset(discussion.getEndLine());
      var markupModel = editor.getMarkupModel();

      markupModel.addRangeHighlighter(startOffset, endOffset, HighlighterLayer.SELECTION, textAttributes, HighlighterTargetArea.EXACT_RANGE);
    }
    editor.addEditorMouseMotionListener(listener);
    editor.addEditorMouseListener(listener);
  }

  private static void transferData(EditorEx editor, DiffRequest request, Key key) {
    editor.putUserData(key, request.getUserData(key));
  }

  private static void applyColorScheme(EditorEx editor) {
    var colorsScheme = editor.getColorsScheme();
    colorsScheme.setAttributes(EditorColors.FOLDED_TEXT_ATTRIBUTES, EDITOR_ATTRIBUTES);
    editor.setColorsScheme(colorsScheme);
  }
}
