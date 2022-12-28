package io.github.askmeagain.pullrequest.gui.toolwindow;

import com.intellij.icons.AllIcons;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.nodes.interfaces.FileNodeMarker;
import io.github.askmeagain.pullrequest.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.services.StateService;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class TradingProjectTreeRenderer extends DefaultTreeCellRenderer {

  private final PullrequestPluginState state = StateService.getInstance().getState();

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                boolean leaf, int row, boolean hasFocus) {

    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

    var image = List.of("");
    var color = "grey";

    setIcon(null);

    if (value instanceof FileNodeMarker) {
      color = state.getFileColor();
    } else if (value instanceof MergeRequestMarker) {
      color = state.getMergeRequestColor();
      var casted = (MergeRequestMarker) value;
      if (casted.getCanBeMerged()) {
        setIcon(AllIcons.Actions.Commit);
        image = casted.getReviewerUrls().stream().map(this::getImageTag).collect(Collectors.toList());
      }
    }

    setText(String.format("<html><span style=\"color:%s\">%s</span>&nbsp;&nbsp;%s</html>", color, value, String.join("", image)));

    return this;
  }

  private String getImageTag(String url) {
    return String.format("<img src=\"%s\" width=\"16\" height=\"16\">", url);
  }
}