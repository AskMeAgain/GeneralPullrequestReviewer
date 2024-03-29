package io.github.askmeagain.pullrequest.listener;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.nodes.interfaces.NodeBehaviour;
import lombok.RequiredArgsConstructor;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.intellij.openapi.ui.popup.JBPopupFactory.ActionSelectionAid.SPEEDSEARCH;

@RequiredArgsConstructor
public class PluginTreeMouseClickListener extends MouseAdapter {
  private final Tree tree;

  public void mouseClicked(MouseEvent me) {
    if (SwingUtilities.isRightMouseButton(me)) {
      var action = (DefaultActionGroup) ActionManager.getInstance().getAction("io.github.askmeagain.pullrequest.group.pullrequests.contextMenu");

      var context = DataManager.getInstance().getDataContext(me.getComponent());

      JBPopupFactory.getInstance()
          .createActionGroupPopup(null, action, context, SPEEDSEARCH, false)
          .showInBestPositionFor(context);
    } else if (SwingUtilities.isLeftMouseButton(me)) {
      var tp = tree.getPathForLocation(me.getX(), me.getY());
      if (tp != null) {
        var lastPathComponent = (DefaultMutableTreeNode) tp.getLastPathComponent();
        if (lastPathComponent instanceof NodeBehaviour) {
          var runnable = (NodeBehaviour) lastPathComponent;
          if (me.getClickCount() == 2) {
            runnable.onDoubleClick();
          } else {
            runnable.onClick();
          }
        }
      }
    }
  }
}