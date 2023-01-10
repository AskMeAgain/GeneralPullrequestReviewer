package io.github.askmeagain.pullrequest.gui.dialogs;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

public class DiscussionPopup {

  public static JBPopup create(List<MergeRequestDiscussion> discussions, BiConsumer<String, String> onSend) {

    //INTENDED use jTabbedPane here because of scroll_tab_layout hidding title
    var tabPanel = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

    var popup = JBPopupFactory.getInstance()
        .createComponentPopupBuilder(tabPanel, tabPanel)
        .setRequestFocus(true)
        .createPopup();

    for (MergeRequestDiscussion discussion : discussions) {
      var discussionPanel = createPopup(discussion, popup, onSend);
      tabPanel.addTab(discussion.getDiscussionId() + "(" + discussion.getReviewComments().size() + ")", discussionPanel);
    }

    return popup;
  }

  @NotNull
  private static JPanel createPopup(MergeRequestDiscussion discussion, JBPopup popup, BiConsumer<String, String> onSend) {
    var textArea = new JTextArea();
    var sendButton = new JButton("Send");

    var existingCommentsPanel = createNewCommentChainPanel(discussion);

    var sendTextField = new JBScrollPane(textArea);

    var dialogPanel = FormBuilder.createFormBuilder()
        .addComponent(existingCommentsPanel)
        .addComponent(sendTextField)
        .addComponent(sendButton)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    sendTextField.setPreferredSize(new Dimension(400, 100));
    existingCommentsPanel.setPreferredSize(new Dimension(400, 200));

    sendButton.addActionListener(actionEvent -> {
      onSend.accept(textArea.getText(), discussion.getDiscussionId());
      popup.cancel();
    });

    return dialogPanel;
  }

  private static JBScrollPane createNewCommentChainPanel(MergeRequestDiscussion discussion) {
    var panelBuilder = FormBuilder.createFormBuilder();

    for (var i = 0; i < discussion.getReviewComments().size(); i++) {
      var label = getTextField(discussion.getReviewComments().get(i).toString());
      panelBuilder = panelBuilder.addSeparator().addComponent(label);
    }

    var panel = panelBuilder.addComponentFillVertically(new JPanel(), 10)
        .getPanel();

    return new JBScrollPane(panel);
  }

  @NotNull
  private static JPanel getTextField(String comment) {
    var fakeLabel = new JTextField(comment);
    fakeLabel.setEditable(false);
    fakeLabel.setBorder(null);
    fakeLabel.setBackground(null);
    fakeLabel.setPreferredSize(new Dimension(317, 50));

    var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panel.add(fakeLabel);
    var preferredSize = new Dimension(30, 30);

    var e = new JButton("E");
    e.setPreferredSize(preferredSize);

    var ee = new JButton("X");
    ee.setPreferredSize(preferredSize);

    panel.add(e);
    panel.add(ee);
    return panel;
  }
}
