package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.gui.dialogs.DiscussionPopup;

import java.util.Optional;

@Service
public final class PopupService {

  public static PopupService getInstance() {
    return ApplicationManager.getApplication().getService(PopupService.class);
  }

  private DiscussionPopup active;

  public void registerPopup(DiscussionPopup discussionPopup) {
    this.active = discussionPopup;
  }

  public Optional<DiscussionPopup> getActive() {
    return Optional.ofNullable(active);
  }
}
