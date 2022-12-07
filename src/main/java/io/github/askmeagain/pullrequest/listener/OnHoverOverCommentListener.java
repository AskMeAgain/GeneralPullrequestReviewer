package io.github.askmeagain.pullrequest.listener;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.ui.popup.JBPopup;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.gui.dialogs.DiscussionPopup;
import io.github.askmeagain.pullrequest.services.DataRequestService;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OnHoverOverCommentListener implements EditorMouseMotionListener, EditorMouseListener {

  private final Map<Integer, MergeRequestDiscussion> linesPerFoldRegionSource;
  private final Map<Integer, MergeRequestDiscussion> linesPerFoldRegionTarget;
  private JBPopup currentActivePopup;

  private int currentActiveLine;
  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  private final String connectionName;

  public OnHoverOverCommentListener(List<MergeRequestDiscussion> comments, String connectionName) {
    linesPerFoldRegionTarget = comments.stream()
        .filter(MergeRequestDiscussion::isSourceDiscussion)
        .collect(Collectors.toMap(MergeRequestDiscussion::getLine, Function.identity()));
    linesPerFoldRegionSource = comments.stream()
        .filter(x -> !x.isSourceDiscussion())
        .collect(Collectors.toMap(MergeRequestDiscussion::getLine, Function.identity()));
    this.connectionName = connectionName;
  }

  @Override
  public void mouseMoved(@NotNull EditorMouseEvent editorMouseEvent) {
    var editor = editorMouseEvent.getEditor();
    var pos = editor.xyToLogicalPosition(new Point(editorMouseEvent.getMouseEvent().getPoint()));
    var isSource = editor.getUserData(TransferKey.IsSource);

    if (linesPerFoldRegionTarget.containsKey(pos.line) && !isSource) {
      createPopup(editorMouseEvent, editor, pos, linesPerFoldRegionTarget.get(pos.line));
    } else if (linesPerFoldRegionSource.containsKey(pos.line) && isSource) {
      createPopup(editorMouseEvent, editor, pos, linesPerFoldRegionSource.get(pos.line));
    }
  }

  private void createPopup(
      EditorMouseEvent e,
      Editor editor,
      LogicalPosition pos,
      MergeRequestDiscussion mergeRequestDiscussion
  ) {
    if (currentActiveLine == pos.line && currentActivePopup.isVisible()) {
      return;
    }

    if (currentActiveLine == pos.line) {
      currentActivePopup.setUiVisible(true);
    }

    if (currentActivePopup != null) {
      currentActivePopup.setUiVisible(false);
    }

    currentActiveLine = pos.line;

    currentActivePopup = DiscussionPopup.create(mergeRequestDiscussion, text -> dataRequestService.addCommentToThread(
        connectionName,
        editor.getUserData(TransferKey.MergeRequestId),
        mergeRequestDiscussion.getDiscussionId(),
        GitlabAddCommentToDiscussionRequest.builder()
            .body(text)
            .build())
    );

    currentActivePopup.showInScreenCoordinates(editor.getComponent(), e.getMouseEvent().getLocationOnScreen());
  }
}
