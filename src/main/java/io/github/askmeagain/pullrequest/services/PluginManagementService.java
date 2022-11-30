package io.github.askmeagain.pullrequest.services;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import lombok.Getter;

import javax.swing.*;

@Service
public final class PluginManagementService {

  @Getter
  private final DefaultListModel<MergeRequest> defaultListModelString = new DefaultListModel<>();

  @Getter(lazy = true)
  private final DataRequestService dataRequestService = DataRequestService.getInstance();

  public void refreshList() {
    System.out.println("Refresh list!");
    defaultListModelString.clear();
    getDataRequestService().getMergeRequests().forEach(defaultListModelString::addElement);
  }

  public static PluginManagementService getInstance() {
    return ApplicationManager.getApplication().getService(PluginManagementService.class);
  }

}
