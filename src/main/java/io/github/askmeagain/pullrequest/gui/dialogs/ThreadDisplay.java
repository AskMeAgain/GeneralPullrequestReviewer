package io.github.askmeagain.pullrequest.gui.dialogs;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ThreadDisplay {

  public static JBPopup create(MergeRequestDiscussion discussion, Consumer<String> onSend) {
    var textArea = new JTextArea(50, 50);
    var sendButton = new JButton("Send");

    var textAreaWrapper = new JBScrollPane(textArea);
    textAreaWrapper.setPreferredSize(new Dimension(300, 300));
    textArea.setSize(new Dimension(300, 300));
    textArea.setPreferredSize(new Dimension(300, 300));
    textArea.setMaximumSize(new Dimension(300, 300));
    textArea.setMinimumSize(new Dimension(300, 300));

    var builder = FormBuilder.createFormBuilder();

    for (var comment : discussion.getReviewComments()) {
      builder = builder.addComponent(new JBLabel(comment.toString(), JLabel.LEFT))
          .addSeparator();
    }

    var component = builder.addComponent(textAreaWrapper)
        .addComponentFillVertically(new JPanel(), 10)
        .addComponent(sendButton)
        .getPanel();

    component.setPreferredSize(new Dimension(300, 200));

    var popup = JBPopupFactory.getInstance()
        .createComponentPopupBuilder(component, textArea)
        .setRequestFocus(true)
        .createPopup();

    sendButton.addActionListener(actionEvent -> {
      onSend.accept(textArea.getText());
      popup.cancel();
    });

    return popup;
  }
}
