package io.github.askmeagain.pullrequest.gui.dialogs;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class DiscussionPopup {

  public static JBPopup create(MergeRequestDiscussion discussion, Consumer<String> onSend) {
    var textArea = new JTextArea();
    var sendButton = new JButton("Send");

    var textAreaWrapper = new JBScrollPane(textArea);
    textAreaWrapper.setSize(new Dimension(395, 200));
    textAreaWrapper.setPreferredSize(new Dimension(395, 200));
    textAreaWrapper.setMaximumSize(new Dimension(395, 200));
    textAreaWrapper.setMinimumSize(new Dimension(395, 200));
    textArea.setSize(new Dimension(395, 200));
    textArea.setPreferredSize(new Dimension(395, 200));
    textArea.setMaximumSize(new Dimension(395, 200));
    textArea.setMinimumSize(new Dimension(395, 200));

    var builder = FormBuilder.createFormBuilder();

    for (var comment : discussion.getReviewComments()) {
      var fakeLabel = new JTextField(comment.toString());
      fakeLabel.setEditable(false);
      fakeLabel.setBorder(null);
      fakeLabel.setBackground(null);
      fakeLabel.setSize(400,50);
      builder = builder.addComponent(fakeLabel).addSeparator();
    }

    var component = builder.addComponent(textAreaWrapper)
        .addComponent(sendButton)
        .addComponentFillVertically(new JPanel(), 10)
        .getPanel();

    component.setPreferredSize(new Dimension(400, 200 + discussion.getReviewComments().size() * 50));

    var popup = JBPopupFactory.getInstance()
        .createComponentPopupBuilder(component, textArea)
        .setRequestFocus(true)
        .createPopup();

    sendButton.addActionListener(actionEvent -> {
      onSend.accept(textArea.getText());
      popup.cancel();
      sendButton.setEnabled(false);
    });

    return popup;
  }
}
