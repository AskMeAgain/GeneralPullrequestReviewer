package io.github.askmeagain.pullrequest.listener;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.ui.popup.JBPopup;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.gui.dialogs.DiscussionPopup;
import io.github.askmeagain.pullrequest.services.DataRequestService;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnHoverOverCommentListener implements EditorMouseMotionListener, EditorMouseListener {

  private final Map<Integer, List<AbstractMap.SimpleEntry<Integer, MergeRequestDiscussion>>> linesPerFoldRegionSource;
  private final Map<Integer, List<AbstractMap.SimpleEntry<Integer, MergeRequestDiscussion>>> linesPerFoldRegionTarget;
  private JBPopup currentActivePopup;

  private int currentActiveLine;
  private boolean currentActiveIsSource;
  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  private final ConnectionConfig connection;

  public OnHoverOverCommentListener(List<MergeRequestDiscussion> comments, ConnectionConfig connection) {
    linesPerFoldRegionTarget = comments.stream()
        .filter(x -> !x.isSourceDiscussion())
        .map(x -> IntStream.range(x.getStartLine(), x.getEndLine())
            .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, x))
            .collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey));

    linesPerFoldRegionSource = comments.stream()
        .filter(MergeRequestDiscussion::isSourceDiscussion)
        .map(x -> IntStream.range(x.getStartLine(), x.getEndLine())
            .mapToObj(i -> new AbstractMap.SimpleEntry<>(i, x))
            .collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey));

    this.connection = connection;
  }

  @Override
  public void mouseMoved(@NotNull EditorMouseEvent editorMouseEvent) {
    var editor = editorMouseEvent.getEditor();
    var pos = editor.xyToLogicalPosition(new Point(editorMouseEvent.getMouseEvent().getPoint()));
    var isSource = editor.getUserData(TransferKey.IsSource);

    var visualPosition = editorMouseEvent.getVisualPosition();

    var offset = editor.visualPositionToOffset(visualPosition);

    var document = editor.getDocument();

    //if cursor is out of line
    if (document.getLineCount() <= pos.line) {
      return;
    }
    if (document.getLineStartOffset(pos.line) < offset && offset > document.getLineEndOffset(pos.line)) {
      return;
    }

    if (linesPerFoldRegionTarget.containsKey(pos.line) && !isSource) {
      createPopup(editorMouseEvent, editor, pos, linesPerFoldRegionTarget.get(pos.line), false);
    } else if (linesPerFoldRegionSource.containsKey(pos.line) && isSource) {
      createPopup(editorMouseEvent, editor, pos, linesPerFoldRegionSource.get(pos.line), true);
    }
  }

  private void createPopup(
      EditorMouseEvent e,
      Editor editor,
      LogicalPosition pos,
      List<AbstractMap.SimpleEntry<Integer, MergeRequestDiscussion>> mergeRequestDiscussion,
      boolean isSource
  ) {
    //already active
    if (currentActivePopup != null && currentActivePopup.isVisible() && currentActiveLine == pos.line && currentActiveIsSource == isSource) {
      return;
    } else if (currentActivePopup != null) {
      //turn off
      currentActivePopup.cancel();
    }

    currentActiveIsSource = isSource;
    currentActiveLine = pos.line;

    var vcsService = dataRequestService.getMapVcsImplementation().get(connection.getVcsImplementation());

    currentActivePopup = DiscussionPopup.create(
        mergeRequestDiscussion,
        (text, discId) -> vcsService.addCommentToThread(
            editor.getUserData(TransferKey.ProjectId),
            connection,
            editor.getUserData(TransferKey.MergeRequestId),
            discId,
            text
        ));

    currentActivePopup.showInScreenCoordinates(editor.getComponent(), e.getMouseEvent().getLocationOnScreen());
  }
}
