package io.github.askmeagain.pullrequest.gui.dialogs;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.TriConsumer;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

public class DiscussionPopup {

  public static JBPopup create(
      List<MergeRequestDiscussion> discussions,
      BiConsumer<String, String> onSend,
      TriConsumer<String, String, Integer> onEditComment,
      BiConsumer<String, Integer> onDeleteComment
  ) {

    //INTENDED use jTabbedPane here because of scroll_tab_layout hiding title
    var tabPanel = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

    var popup = JBPopupFactory.getInstance()
        .createComponentPopupBuilder(tabPanel, tabPanel)
        .setRequestFocus(true)
        .createPopup();

    for (MergeRequestDiscussion discussion : discussions) {
      var discussionPanel = createPopup(discussion, popup, onSend, onEditComment, onDeleteComment);
      tabPanel.addTab(discussion.getDiscussionId() + "(" + discussion.getReviewComments().size() + ")", discussionPanel);
    }

    return popup;
  }

  @NotNull
  private static JPanel createPopup(
      MergeRequestDiscussion discussion,
      JBPopup popup,
      BiConsumer<String, String> onSend,
      TriConsumer<String, String, Integer> onEditComment,
      BiConsumer<String, Integer> onDeleteComment
  ) {
    var textArea = new JTextArea();
    var sendButton = new JButton("Send");

    var existingCommentsPanel = createNewCommentChainPanel(discussion, textArea, onEditComment, onDeleteComment);

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

  private static JBScrollPane createNewCommentChainPanel(
      MergeRequestDiscussion discussion,
      JTextArea textArea,
      TriConsumer<String, String, Integer> onEditComment,
      BiConsumer<String, Integer> onDeleteComment
  ) {
    var panelBuilder = FormBuilder.createFormBuilder();

    for (var i = 0; i < discussion.getReviewComments().size(); i++) {
      var label = getTextField(
          textArea,
          discussion.getReviewComments().get(i).toString(),
          discussion.getReviewComments().get(i).getNoteId(),
          discussion.getDiscussionId(),
          onEditComment,
          onDeleteComment
      );
      panelBuilder = panelBuilder.addSeparator().addComponent(label);
    }

    var panel = panelBuilder.addComponentFillVertically(new JPanel(), 10)
        .getPanel();

    return new JBScrollPane(panel);
  }

  @NotNull
  private static JPanel getTextField(
      JTextArea textArea,
      String comment,
      Integer noteId,
      String discussionId,
      TriConsumer<String, String, Integer> onEditComment,
      BiConsumer<String, Integer> onDeleteComment
  ) {
    var fakeLabel = new JTextField(comment);
    fakeLabel.setEditable(false);
    fakeLabel.setBorder(null);
    fakeLabel.setBackground(null);
    fakeLabel.setPreferredSize(new Dimension(317, 50));

    var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panel.add(fakeLabel);
    var preferredSize = new Dimension(30, 30);

    var e = new JButton("E");
    e.addActionListener(l -> onEditComment.consume(textArea.getText(), discussionId, noteId));
    e.setPreferredSize(preferredSize);
    panel.add(e);

    var ee = new JButton("X");
    ee.addActionListener(l -> onDeleteComment.accept(discussionId, noteId));
    ee.setPreferredSize(preferredSize);
    panel.add(ee);

    return panel;
  }
}
