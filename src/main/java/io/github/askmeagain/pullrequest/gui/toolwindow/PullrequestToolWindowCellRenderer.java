package io.github.askmeagain.pullrequest.gui.toolwindow;

import com.intellij.openapi.fileChooser.tree.FileNode;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.gui.nodes.gitlab.GitlabFileNode;
import io.github.askmeagain.pullrequest.services.StateService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.intellij.openapi.fileChooser.FileElement.isFileHidden;
import static com.intellij.openapi.util.IconLoader.getTransparentIcon;

public class PullrequestToolWindowCellRenderer extends ColoredTreeCellRenderer {

  private static final Color GRAYED = SimpleTextAttributes.GRAYED_ATTRIBUTES.getFgColor();

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
    int style = SimpleTextAttributes.STYLE_PLAIN;
    Color color = null;
    String name = null;
    String comment = null;
    boolean valid = true;
    if (value instanceof GitlabFileNode) {
      color = Color.decode(state.getFileColor());
    } else if (value instanceof VirtualFile) {
      VirtualFile file = (VirtualFile) value;
      name = file.getName();
      valid = file.isValid();
    } else if (value != null) {
      name = value.toString(); //NON-NLS
      color = GRAYED;
    }

    if (!valid) style |= SimpleTextAttributes.STYLE_STRIKEOUT;
    setIcon(null);
    SimpleTextAttributes attributes = new SimpleTextAttributes(style, color);
    if (name != null) append(name, attributes);
    if (comment != null) append(comment, attributes);
  }

}