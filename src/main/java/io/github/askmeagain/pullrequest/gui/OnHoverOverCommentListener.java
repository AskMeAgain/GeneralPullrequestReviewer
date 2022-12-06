package io.github.askmeagain.pullrequest.gui;

import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.ui.popup.JBPopup;
import io.github.askmeagain.pullrequest.dto.application.CommentRequest;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.gui.dialogs.ThreadDisplay;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OnHoverOverCommentListener implements EditorMouseMotionListener, EditorMouseListener {

  private final Map<Integer, MergeRequestDiscussion> linesPerFoldRegion;
  private JBPopup currentActivePopup;

  private int currentActiveLine;


  @Getter(lazy = true)
  private final PluginManagementService pullrequestService = PluginManagementService.getInstance();

  public OnHoverOverCommentListener(List<MergeRequestDiscussion> comments) {
    linesPerFoldRegion = comments.stream()
        .collect(Collectors.toMap(MergeRequestDiscussion::getLine, Function.identity()));
  }

  @Override
  public void mouseMoved(@NotNull EditorMouseEvent e) {
    var editor = e.getEditor();
    var mouseEvent = e.getMouseEvent();

    var point = new Point(mouseEvent.getPoint());
    var pos = editor.xyToLogicalPosition(point);

    if (linesPerFoldRegion.containsKey(pos.line)) {
      var reviewComment = linesPerFoldRegion.get(pos.line);

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

      var mergeRequestId = editor.getUserData(MouseClickListener.MergeRequestId);

      var discId = reviewComment.getDiscussionId();
      currentActivePopup = ThreadDisplay.create(reviewComment, text -> getPullrequestService()
          .addCommentToThread(mergeRequestId, discId, GitlabAddCommentToDiscussionRequest.builder()
              .body(text)
              .build()));
      currentActivePopup.showInScreenCoordinates(editor.getComponent(), e.getMouseEvent().getLocationOnScreen());
    }
  }
}
