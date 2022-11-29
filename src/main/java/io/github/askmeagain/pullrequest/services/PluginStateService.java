package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.MergeRequest;
import lombok.Getter;

import javax.swing.*;

@Service
public final class PluginStateService {

  @Getter
  private final DefaultListModel<MergeRequest> defaultListModelString = new DefaultListModel<>();

  @Getter(lazy = true)
  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  public void refreshList() {
    System.out.println("Refresh list!");
    defaultListModelString.clear();
    getDataRequestService().getMergeRequests().forEach(defaultListModelString::addElement);
  }

  public static PluginStateService getInstance() {
    return ApplicationManager.getApplication().getService(PluginStateService.class);
  }

}
