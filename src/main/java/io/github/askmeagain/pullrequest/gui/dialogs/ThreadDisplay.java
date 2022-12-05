package io.github.askmeagain.pullrequest.gui.dialogs;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ThreadDisplay {

  public static JBPopup create(String text, Consumer<String> onSend) {
    var textArea = new JTextArea(5, 10);
    var sendButton = new JButton("Send");

    var textAreaWrapper = new JBScrollPane(textArea);
    textAreaWrapper.setPreferredSize(new Dimension(300, 300));
    textArea.setPreferredSize(new Dimension(300, 300));

    var component = FormBuilder.createFormBuilder()
        .addComponent(new JBLabel(text, JLabel.LEFT))
        .addSeparator()
        .addComponent(textAreaWrapper)
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
