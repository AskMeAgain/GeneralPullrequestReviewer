package io.github.askmeagain.pullrequest.listener;

import com.intellij.openapi.editor.Editor;
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
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnHoverOverCommentListener implements EditorMouseMotionListener, EditorMouseListener {

  private final Map<Integer, List<MergeRequestDiscussion>> linesPerFoldRegionSource;
  private final Map<Integer, List<MergeRequestDiscussion>> linesPerFoldRegionTarget;
  private JBPopup currentActivePopup;

  private String currentActiveDiscussionId;
  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  private final ConnectionConfig connection;

  public OnHoverOverCommentListener(List<MergeRequestDiscussion> comments, ConnectionConfig connection) {
    linesPerFoldRegionTarget = comments.stream()
        .filter(x -> !x.isSourceDiscussion())
        .map(x -> IntStream.range(x.getStartLine(), x.getEndLine() + 1)
            .mapToObj(i -> new SimpleEntry<>(i, x))
            .collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, Collectors.mapping(SimpleEntry::getValue, Collectors.toList())));

    linesPerFoldRegionSource = comments.stream()
        .filter(MergeRequestDiscussion::isSourceDiscussion)
        .map(x -> IntStream.range(x.getStartLine(), x.getEndLine() + 1)
            .mapToObj(i -> new SimpleEntry<>(i, x))
            .collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, Collectors.mapping(SimpleEntry::getValue, Collectors.toList())));

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
      createPopup(editorMouseEvent, editor, linesPerFoldRegionTarget.get(pos.line));
    } else if (linesPerFoldRegionSource.containsKey(pos.line) && isSource) {
      createPopup(editorMouseEvent, editor, linesPerFoldRegionSource.get(pos.line));
    }
  }

  private void createPopup(EditorMouseEvent e, Editor editor, List<MergeRequestDiscussion> mergeRequestDiscussion) {
    //already active
    if (currentActivePopup != null && currentActivePopup.isVisible() &&
        mergeRequestDiscussion.stream().anyMatch(x -> x.getDiscussionId().equals(currentActiveDiscussionId))
    ) {
      return;
    } else if (currentActivePopup != null) {
      //turn off
      currentActivePopup.cancel();
    }

    currentActiveDiscussionId = mergeRequestDiscussion.get(0).getDiscussionId();

    var vcsService = dataRequestService.getMapVcsImplementation().get(connection.getVcsImplementation());

    currentActivePopup = DiscussionPopup.create(
        mergeRequestDiscussion,
        (text, discId) -> vcsService.addCommentToThread(
            editor.getUserData(TransferKey.ProjectId),
            connection,
            editor.getUserData(TransferKey.MergeRequestId),
            discId,
            text
        ),
        (text, discId, noteId) -> vcsService.editComment(
            connection,
            editor.getUserData(TransferKey.ProjectId),
            editor.getUserData(TransferKey.MergeRequestId),
            discId,
            String.valueOf(noteId),
            text
        ),
        (discId, noteId) ->
            vcsService.deleteComment(
                connection,
                editor.getUserData(TransferKey.ProjectId),
                editor.getUserData(TransferKey.MergeRequestId),
                discId,
                String.valueOf(noteId)
            )
    );

    currentActivePopup.showInScreenCoordinates(editor.getComponent(), e.getMouseEvent().getLocationOnScreen());
  }
}
