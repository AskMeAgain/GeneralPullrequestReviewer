package io.github.askmeagain.pullrequest.gui.toolwindow;

import com.intellij.openapi.fileChooser.tree.FileNode;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.intellij.openapi.fileChooser.FileElement.isFileHidden;
import static com.intellij.openapi.util.IconLoader.getTransparentIcon;

public class PullrequestToolWindowCellRenderer extends ColoredTreeCellRenderer {

  private static final Color GRAYED = SimpleTextAttributes.GRAYED_ATTRIBUTES.getFgColor();
  private static final Color HIDDEN = SimpleTextAttributes.DARK_TEXT.getFgColor();

  @Override
  public void customizeCellRenderer(@NotNull JTree tree, Object value,
                                    boolean selected, boolean expanded, boolean leaf, int row, boolean focused) {
    customize(this, value, selected, focused);
  }

  protected void customize(SimpleColoredComponent renderer, Object value, boolean selected, boolean focused) {
    int style = SimpleTextAttributes.STYLE_PLAIN;
    Color color = null;
    Icon icon = null;
    String name = null;
    String comment = null;
    boolean hidden = false;
    boolean valid = true;
    if (value instanceof FileNode) {
      FileNode node = (FileNode)value;
      icon = node.getIcon();
      name = node.getName();
      comment = node.getComment();
      hidden = node.isHidden();
      valid = node.isValid();
    }
    else if (value instanceof VirtualFile) {
      VirtualFile file = (VirtualFile)value;
      name = file.getName();
      hidden = isFileHidden(file);
      valid = file.isValid();
    }
    else if (value != null) {
      name = value.toString(); //NON-NLS
      color = GRAYED;
    }
    if (!valid) style |= SimpleTextAttributes.STYLE_STRIKEOUT;
    if (hidden) color = HIDDEN;
    renderer.setIcon(!hidden || icon == null ? icon : getTransparentIcon(icon));
    SimpleTextAttributes attributes = new SimpleTextAttributes(style, color);
    if (name != null) renderer.append(name, attributes);
    if (comment != null) renderer.append(comment, attributes);
  }
}