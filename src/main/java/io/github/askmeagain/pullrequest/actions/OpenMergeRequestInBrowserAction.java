package io.github.askmeagain.pullrequest.actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import io.github.askmeagain.pullrequest.nodes.interfaces.MergeRequestMarker;
import io.github.askmeagain.pullrequest.nodes.interfaces.NodeBehaviour;
import io.github.askmeagain.pullrequest.services.ManagementService;

import java.net.URI;
import java.net.URISyntaxException;

public class OpenMergeRequestInBrowserAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    var lastSelectedPathComponent = (NodeBehaviour) ManagementService.getInstance()
        .getTree()
        .getLastSelectedPathComponent();

    if (lastSelectedPathComponent instanceof MergeRequestMarker) {
      var casted = (MergeRequestMarker) lastSelectedPathComponent;
      try {
        BrowserUtil.browse(new URI(casted.getMergeRequestUrl()));
      } catch (URISyntaxException ex) {
        throw new RuntimeException(ex);
      }
    }
  }
}
