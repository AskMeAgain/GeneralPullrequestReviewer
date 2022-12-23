package io.github.askmeagain.pullrequest.gui.toolwindow;

import com.intellij.icons.AllIcons;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.nodes.interfaces.FileNodeMarker;
import io.github.askmeagain.pullrequest.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.services.StateService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PullrequestToolWindowCellRenderer extends ColoredTreeCellRenderer {

  private static final Color GRAYED = SimpleTextAttributes.REGULAR_ATTRIBUTES.getFgColor();

  private final PullrequestPluginState state = StateService.getInstance().getState();

  @Override
  public void customizeCellRenderer(
      @NotNull JTree tree,
      Object value,
      boolean selected,
      boolean expanded,
      boolean leaf,
      int row,
      boolean focused
  ) {
    var color = GRAYED;

    setIcon(null);

    if (value instanceof FileNodeMarker) {
      color = Color.decode(state.getFileColor());
    } else if (value instanceof MergeRequestMarker) {
      color = Color.decode(state.getMergeRequestColor());
      var casted = (MergeRequestMarker) value;
      if (casted.getCanBeMerged()) {
        setIcon(AllIcons.Actions.Commit);
      }
    }

    var attributes = new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, color);
    append(value.toString(), attributes);
  }
}