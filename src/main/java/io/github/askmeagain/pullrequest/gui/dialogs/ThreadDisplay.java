package io.github.askmeagain.pullrequest.gui.dialogs;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.awt.*;

public class ThreadDisplay {

  public static JBPopup create(String text) {
    var textArea = new JTextArea(5, 10);
    var sendButton = new JButton("Send");
    var cancel = new JButton("Cancel");

    //TODO
    sendButton.addActionListener(actionEvent -> System.out.println(textArea.getText()));

    var component = FormBuilder.createFormBuilder()
        .addComponent(new JBLabel(text, JLabel.TRAILING))
        .addSeparator()
        .addComponent(textArea)
        .addComponentFillVertically(new JPanel(), 10)
        .addLabeledComponent(sendButton, cancel)
        .getPanel();

    component.setPreferredSize(new Dimension(300, 200));

    component.setBorder(BorderFactory.createTitledBorder("Abc"));

    var popup = JBPopupFactory.getInstance()
        .createComponentPopupBuilder(component, textArea)
        .setRequestFocus(true)
        .createPopup();

    cancel.addActionListener(actionEvent -> {
      popup.cancel();
    });

    return popup;
  }
}
