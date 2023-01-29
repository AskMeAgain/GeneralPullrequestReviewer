package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.TransferKey;
import io.github.askmeagain.pullrequest.gui.dialogs.DiscussionPopup;
import io.github.askmeagain.pullrequest.nodes.interfaces.NodeBehaviour;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class OnHoverOverCommentListener implements EditorMouseMotionListener, EditorMouseListener {

  private final NodeBehaviour fileNode;
  private final ConnectionConfig connection;

  private Map<Integer, List<MergeRequestDiscussion>> linesPerFoldRegionSource;
  private Map<Integer, List<MergeRequestDiscussion>> linesPerFoldRegionTarget;

  private Optional<DiscussionPopup> currentActivePopup = Optional.empty();
  private String currentActiveDiscussionId;
  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  public OnHoverOverCommentListener(NodeBehaviour fileNode, List<MergeRequestDiscussion> comments, ConnectionConfig connection) {
    this.connection = connection;
    this.fileNode = fileNode;

    setFoldLineData(comments);
  }

  public void refresh(List<MergeRequestDiscussion> discussions) {
    setFoldLineData(discussions);
    currentActivePopup.ifPresent(popup -> popup.refresh(discussions));
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
      createPopup(editorMouseEvent, editor, linesPerFoldRegionTarget.get(pos.line), pos.line);
    } else if (linesPerFoldRegionSource.containsKey(pos.line) && isSource) {
      createPopup(editorMouseEvent, editor, linesPerFoldRegionSource.get(pos.line), pos.line);
    }
  }

  private void createPopup(EditorMouseEvent e, Editor editor, List<MergeRequestDiscussion> mergeRequestDiscussion, int line) {
    //already active
    if (currentActivePopup.isPresent() && currentActivePopup.get().getPopup().isVisible() &&
        mergeRequestDiscussion.stream().anyMatch(x -> x.getDiscussionId().equals(currentActiveDiscussionId))
    ) {
      return;
    } else if (currentActivePopup.isPresent()) {
      //turn off
      currentActivePopup.get().getPopup().cancel();
    }

    currentActiveDiscussionId = mergeRequestDiscussion.get(0).getDiscussionId();

    var vcsService = dataRequestService.getMapVcsImplementation().get(connection.getVcsImplementation());

    var popup = new DiscussionPopup(
        line,
        fileNode::refresh,
        mergeRequestDiscussion,
        (discId) -> vcsService.resolveComment(),
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
        (discId, noteId) -> vcsService.deleteComment(
            connection,
            editor.getUserData(TransferKey.ProjectId),
            editor.getUserData(TransferKey.MergeRequestId),
            discId,
            String.valueOf(noteId)
        )
    );

    popup.getPopup().showInScreenCoordinates(editor.getComponent(), e.getMouseEvent().getLocationOnScreen());

    currentActivePopup = Optional.of(popup);
  }

  private void setFoldLineData(List<MergeRequestDiscussion> comments) {
    linesPerFoldRegionTarget = comments.stream()
        .filter(x -> !x.isSourceDiscussion())
        .map(x -> IntStream.range(x.getStartLine(), x.getEndLine() + 1)
            .mapToObj(i -> new SimpleEntry<>(i, x))
            .collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(SimpleEntry::getKey, Collectors.mapping(SimpleEntry::getValue, Collectors.toList())));

    linesPerFoldRegionSource = comments.stream()
        .filter(MergeRequestDiscussion::isSourceDiscussion)
        .map(x -> IntStream.range(x.getStartLine(), x.getEndLine() + 1)
            .mapToObj(i -> new SimpleEntry<>(i, x))
            .collect(Collectors.toList()))
        .flatMap(Collection::stream)
        .collect(Collectors.groupingBy(SimpleEntry::getKey, Collectors.mapping(SimpleEntry::getValue, Collectors.toList())));
  }
}
