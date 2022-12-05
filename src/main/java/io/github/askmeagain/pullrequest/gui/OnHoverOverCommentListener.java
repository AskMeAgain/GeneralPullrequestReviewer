package io.github.askmeagain.pullrequest.gui;

import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.gui.dialogs.ThreadDisplay;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OnHoverOverCommentListener implements EditorMouseMotionListener, EditorMouseListener {

  private final Map<Integer, ReviewComment> linesPerFoldRegion;
  private JBPopup currentActivePopup;

  private int currentActiveLine;

  public OnHoverOverCommentListener(List<ReviewComment> comments) {
    linesPerFoldRegion = comments.stream()
        .collect(Collectors.toMap(ReviewComment::getLine, Function.identity()));
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

      currentActivePopup = ThreadDisplay.create(reviewComment.toString());
      currentActivePopup.showInScreenCoordinates(editor.getComponent(), e.getMouseEvent().getLocationOnScreen());
    }
  }
}
