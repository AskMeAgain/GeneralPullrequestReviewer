package io.github.askmeagain.pullrequest.gui.dialogs;

import com.intellij.diff.requests.DiffRequest;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.Key;
import com.intellij.ui.JBColor;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.listener.OnHoverOverCommentListener;
import io.github.askmeagain.pullrequest.services.StateService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DiffView {

  private final DiffRequest request;
  private final EditorEx sourceEditor;
  private final EditorEx targetEditor;
  private OnHoverOverCommentListener listener;

  public String getId() {
    return request.getUserData(TransferKey.DataContextKeyTarget).getFileName();
  }

  private static final TextAttributes EDITOR_ATTRIBUTES = new TextAttributes(JBColor.red, null, null, null, Font.BOLD);

  private final PullrequestPluginState state = StateService.getInstance().getState();

  private final java.util.List<Key> keys = List.of(
      TransferKey.ProjectId,
      TransferKey.CommitId,
      TransferKey.Connection,
      TransferKey.FileName,
      TransferKey.MergeRequestId,
      TransferKey.FileHunk
  );

  public void create() {
    sourceEditor.putUserData(TransferKey.IsSource, true);
    targetEditor.putUserData(TransferKey.IsSource, false);

    listener = new OnHoverOverCommentListener(
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

    var reviewDiscussion = new ArrayList<>(request.getUserData(fileKey).getReviewDiscussions());

    createInternal(editor, listener, reviewDiscussion);
  }

  @NotNull
  private TextAttributes getTextAttributes() {
    return new TextAttributes(null, Color.decode(state.getDiscussionTextColor()), null, null, Font.PLAIN);
  }

  private void createInternal(EditorEx editor, OnHoverOverCommentListener listener, List<MergeRequestDiscussion> reviewDiscussion) {
    var textAttributes = getTextAttributes();

    editor.getMarkupModel().removeAllHighlighters();

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

  public void refresh(List<MergeRequestDiscussion> comments) {
    //TODO refresh listener

    var sourceComments = comments.stream().filter(MergeRequestDiscussion::isSourceDiscussion).collect(Collectors.toList());
    var targetComments = comments.stream().filter(x -> !x.isSourceDiscussion()).collect(Collectors.toList());

    createInternal(sourceEditor, listener, sourceComments);
    createInternal(targetEditor, listener, targetComments);
  }
}
