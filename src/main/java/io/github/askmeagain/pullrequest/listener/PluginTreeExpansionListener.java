package io.github.askmeagain.pullrequest.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.nodes.interfaces.NodeBehaviour;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public final class PluginTreeExpansionListener implements TreeWillExpandListener {

  @Getter
  private final AtomicBoolean activateTriggers = new AtomicBoolean(true);

  public void doWithoutTriggers(Runnable runnable){
    activateTriggers.set(false);
    runnable.run();
    activateTriggers.set(true);
  }

  public static PluginTreeExpansionListener getInstance() {
    return ApplicationManager.getApplication().getService(PluginTreeExpansionListener.class);
  }

  @Override
  public void treeWillExpand(TreeExpansionEvent treeExpansionEvent) {
    if (!activateTriggers.get()) {
      return;
    }

    var lastNode = (DefaultMutableTreeNode) treeExpansionEvent.getPath().getLastPathComponent();

    if (lastNode instanceof NodeBehaviour) {
      var node = (NodeBehaviour) lastNode;
      node.beforeExpanded();
    }
  }

  @Override
  public void treeWillCollapse(TreeExpansionEvent treeExpansionEvent) {
    //do nothing
  }
}
