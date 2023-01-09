package io.github.askmeagain.pullrequest.gui.dialogs;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
import java.util.List;
import java.util.function.BiConsumer;

public class DiscussionPopup {

  public static JBPopup create(List<AbstractMap.SimpleEntry<Integer, MergeRequestDiscussion>> discussion, BiConsumer<String, String> onSend) {
    var textArea = new JTextArea();
    var sendButton = new JButton("Send");

    var existingCommentsPanel = createNewCommentChainPanel(discussion.get(0).getValue());

    var sendTextField = new JBScrollPane(textArea);

    var dialogPanel = FormBuilder.createFormBuilder()
        .addComponent(existingCommentsPanel)
        .addComponent(sendTextField)
        .addComponent(sendButton)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    sendTextField.setPreferredSize(new Dimension(400, 100));
    existingCommentsPanel.setPreferredSize(new Dimension(400, 200));

    var popup = JBPopupFactory.getInstance()
        .createComponentPopupBuilder(dialogPanel, textArea)
        .setRequestFocus(true)
        .createPopup();

//    sendButton.addActionListener(actionEvent -> {
//      onSend.accept(textArea.getText(), discussion.getDiscussionId());
//      popup.cancel();
//    });

    return popup;
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
  private static JTextField getTextField(String comment) {
    var fakeLabel = new JTextField(comment);
    fakeLabel.setEditable(false);
    fakeLabel.setBorder(null);
    fakeLabel.setBackground(null);
    fakeLabel.setSize(400, 50);
    return fakeLabel;
  }
}
