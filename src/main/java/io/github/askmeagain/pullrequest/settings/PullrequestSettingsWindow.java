package io.github.askmeagain.pullrequest.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.*;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

public class PullrequestSettingsWindow {

  @Getter
  private final JBTabbedPane tabbedPane;

  private final JBPasswordField gitlabApiToken = new JBPasswordField();
  private final JBTextField gitlabUrl = new JBTextField();
  private final JBTextField groupId = new JBTextField();
  private final JBTextField gitlabProjects = new JBTextField();
  private final JComboBox<VcsImplementation> selectedVcsImplementation = new ComboBox<>(new VcsImplementation[]{
      VcsImplementation.GITLAB,
      VcsImplementation.GITHUB
  });

  public PullrequestSettingsWindow() {

    tabbedPane = new JBTabbedPane();

    var general = FormBuilder.createFormBuilder()
        .addLabeledComponent("Select vcs integration", selectedVcsImplementation)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    var addProjectButton = new JButton("Add Project");
    addProjectButton.addActionListener(a -> {

      if (selectedVcsImplementation.getSelectedItem() == VcsImplementation.GITLAB) {
        var gitlabIntegration = FormBuilder.createFormBuilder()
            .addLabeledComponent(new JBLabel("Gitlab Api token"), gitlabApiToken, 1, false)
            .addLabeledComponent(new JBLabel("Gitlab url"), gitlabUrl, 1, false)
            .addLabeledComponent(new JBLabel("Group id"), groupId, 1, false)
            .addLabeledComponent(new JBLabel("Projects"), gitlabProjects, 1, false)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();

        tabbedPane.add(gitlabIntegration, tabbedPane.getSelectedIndex());
      } else {
        System.out.println("Not implemented");
      }
    });

    var addTab = FormBuilder.createFormBuilder()
        .addLabeledComponent("Select vcs integration", selectedVcsImplementation)
        .addComponent(addProjectButton)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    tabbedPane.addTab("General", general);
    tabbedPane.addTab("New", addTab);
  }

  public JComponent getPreferredFocusedComponent() {
    return gitlabApiToken;
  }

  public String getGitlabApiToken() {
    return new String(gitlabApiToken.getPassword());
  }

  public void setGitlabApiToken(String token) {
    gitlabApiToken.setText(token);
  }

  public String getGitlabUrl() {
    return gitlabUrl.getText();
  }

  public void setGitlabUrl(String url) {
    gitlabUrl.setText(url);
  }

  public String getGitlabGroup() {
    return groupId.getText();
  }

  public void setGitlabGroup(String groupId) {
    this.groupId.setText(groupId);
  }

  public List<String> getGitlabProjects() {
    return List.of(gitlabProjects.getText().split(","));
  }

  public void setGitlabProjects(List<String> projectIds) {
    gitlabProjects.setText(String.join(",", projectIds));
  }

  public VcsImplementation getVcsImplementation() {
    return (VcsImplementation) selectedVcsImplementation.getSelectedItem();
  }

  public void setVcsImplementation(VcsImplementation selectedItem) {
    selectedVcsImplementation.setSelectedItem(selectedItem);
  }

}