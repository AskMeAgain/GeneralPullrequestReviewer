package io.github.askmeagain.pullrequest.listener;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.ex.FoldingModelEx;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnHoverOverCommentListener implements EditorMouseMotionListener, EditorMouseListener {

  private final List<ReviewComment> comments;
  private final List<FoldRegion> foldRegionList;
  private final FoldingModelEx foldingModelEx;

  private final Map<Integer, FoldRegion> linesPerFoldRegion = new HashMap<>();

  public OnHoverOverCommentListener(List<ReviewComment> comments, List<FoldRegion> foldRegionList, FoldingModelEx foldingModel) {
    this.comments = comments;
    this.foldRegionList = foldRegionList;

    for (int i = 0; i < comments.size(); i++) {
      linesPerFoldRegion.put(comments.get(i).getLine() + i, foldRegionList.get(i));
    }
    foldingModelEx = foldingModel;
  }

  @Override
  public void mouseReleased(@NotNull EditorMouseEvent e) {
    Editor editor = e.getEditor();
    MouseEvent mouseEvent = e.getMouseEvent();

    Point point = new Point(mouseEvent.getPoint());
    LogicalPosition pos = editor.xyToLogicalPosition(point);

    if (linesPerFoldRegion.containsKey(pos.line)) {
      var foldRegion = linesPerFoldRegion.get(pos.line);
      if (foldRegion.isExpanded()) {
        foldingModelEx.runBatchFoldingOperation(() -> foldRegion.setExpanded(false));
      }
    }
  }

  @Override
  public void mouseMoved(EditorMouseEvent e) {


//    comments.stream()
//        .filter(x -> x.getLine() == pos.line)
//        .findFirst()
//        .ifPresent(comment -> System.out.println(comment.getText()));
  }

}
