package io.github.askmeagain.pullrequest.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.askmeagain.pullrequest.nodes.interfaces.NodeBehaviour;
import io.github.askmeagain.pullrequest.nodes.interfaces.OpenUrlMarker;
import io.github.askmeagain.pullrequest.services.ManagementService;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

public class OpenUrlInBrowserAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    var lastSelectedPathComponent = (NodeBehaviour) ManagementService.getInstance()
        .getTree()
        .getLastSelectedPathComponent();

    if (lastSelectedPathComponent instanceof OpenUrlMarker) {
      var casted = (OpenUrlMarker) lastSelectedPathComponent;
      try {
        BrowserUtil.browse(new URI(casted.getUrl()));
      } catch (URISyntaxException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    var lastSelectedPathComponent = (NodeBehaviour) ManagementService.getInstance()
        .getTree()
        .getLastSelectedPathComponent();

    e.getPresentation().setEnabled(false);

    if (lastSelectedPathComponent instanceof OpenUrlMarker) {
      e.getPresentation().setEnabled(true);
    }
  }
}
