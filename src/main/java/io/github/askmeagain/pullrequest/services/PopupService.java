package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.gui.dialogs.DiscussionPopup;
import lombok.Getter;

@Service
public final class PopupService {

  public static PopupService getInstance() {
    return ApplicationManager.getApplication().getService(PopupService.class);
  }

  @Getter
  private DiscussionPopup active;

  public void registerPopup(DiscussionPopup discussionPopup) {
    this.active = discussionPopup;
  }
}
