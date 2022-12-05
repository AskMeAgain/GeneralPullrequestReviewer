package io.github.askmeagain.pullrequest.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.gui.nodes.FileNodes;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@RequiredArgsConstructor
public class MouseClickListener extends MouseAdapter {
  private final Tree tree;

  public static final Key<ReviewFile> DataContextKeySource = Key.create("source");
  public static final Key<ReviewFile> DataContextKeyTarget = Key.create("target");
  public static final Key<Boolean> IsSource = Key.create("isSource");
  public static final Key<String> MergeRequestId = Key.create("mergeRequestId");
  public static final Key<String> FileName = Key.create("fileName");

  @Getter(lazy = true)
  private final PluginManagementService pluginManagementService = PluginManagementService.getInstance();

  public void mouseClicked(MouseEvent me) {
    TreePath tp = tree.getPathForLocation(me.getX(), me.getY());
    if (tp != null) {
      var lastPathComponent = (DefaultMutableTreeNode) tp.getLastPathComponent();
      if (lastPathComponent.getUserObject() instanceof FileNodes) {
        var runnable = (FileNodes) lastPathComponent.getUserObject();
        runnable.openFile();
      }
    }
  }
}
