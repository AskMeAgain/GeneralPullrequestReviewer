package io.github.askmeagain.pullrequest;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.util.TextRange;
import io.github.askmeagain.pullrequest.dto.ReviewComment;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

@RequiredArgsConstructor
public class OnHoverOverCommentListener implements EditorMouseMotionListener {

  private final List<ReviewComment> comments;

  @Override
  public void mouseMoved(EditorMouseEvent e) {
    Editor editor = e.getEditor();
    MouseEvent mouseEvent = e.getMouseEvent();

    Point point = new Point(mouseEvent.getPoint());
    LogicalPosition pos = editor.xyToLogicalPosition(point);
    int offset = editor.logicalPositionToOffset(pos);

    comments.stream()
        .filter(x -> x.getTextRange().contains(offset))
        .findFirst()
        .ifPresent(comment -> System.out.println(comment.getText()));
  }

}
