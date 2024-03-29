package io.github.askmeagain.pullrequest.gui.dialogs;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.TriConsumer;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DiscussionPopup {

  private final Runnable refresh;
  private final boolean isResolvable;
  private final BiConsumer<String, String> onSend;
  private final Consumer<String> onResolve;
  private final TriConsumer<String, String, String> onEditComment;
  private final BiConsumer<String, String> onDeleteComment;
  private final JTabbedPane tabPanel;
  private final JTextArea textArea = new JTextArea();
  private final JButton sendButton = new JButton("Send");
  private final JButton resolveButton = new JButton("Resolve");
  @Getter
  private String id;
  private int line;
  private boolean isSource;
  @Getter
  private final JBPopup popup;

  public DiscussionPopup(
      int line,
      Runnable refresh,
      List<MergeRequestDiscussion> discussions,
      boolean isResolvable,
      Consumer<String> onResolve,
      BiConsumer<String, String> onSend,
      TriConsumer<String, String, String> onEditComment,
      BiConsumer<String, String> onDeleteComment
  ) {
    this.onResolve = onResolve;
    this.isResolvable = isResolvable;
    this.line = line;
    this.onSend = onSend;
    this.refresh = refresh;
    this.onEditComment = onEditComment;
    this.onDeleteComment = onDeleteComment;

    //INTENDED use jTabbedPane here because of scroll_tab_layout hiding title
    tabPanel = new JTabbedPane(SwingConstants.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

    popup = JBPopupFactory.getInstance()
        .createComponentPopupBuilder(tabPanel, tabPanel)
        .setRequestFocus(true)
        .createPopup();

    isSource = discussions.get(0).isSourceDiscussion();

    refresh(discussions);
  }

  public void refresh(List<MergeRequestDiscussion> discussions) {
    tabPanel.removeAll();

    discussions.stream()
        .filter(x -> x.isSourceDiscussion() == isSource)
        .filter(x -> x.getStartLine() <= line && line <= x.getEndLine())
        .forEach(discussion -> {
          id = discussion.getDiscussionId();
          var discussionPanel = createPanel(discussion);
          tabPanel.addTab(discussion.getDiscussionId() + "(" + discussion.getReviewComments().size() + ")", discussionPanel);
        });

    tabPanel.repaint();
  }

  private JPanel createPanel(MergeRequestDiscussion discussion) {
    var commentScrollPane = new JBScrollPane(createNewCommentChainPanel(discussion));
    var sendTextField = new JBScrollPane(textArea);

    var dialogPanel = FormBuilder.createFormBuilder()
        .addComponent(commentScrollPane)
        .addComponent(sendTextField)
        .addLabeledComponent(sendButton, resolveButton)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    sendTextField.setPreferredSize(new Dimension(400, 100));
    commentScrollPane.setPreferredSize(new Dimension(400, 200));

    resolveButton.setEnabled(isResolvable);

    sendButton.addActionListener(actionEvent -> {
      onSend.accept(textArea.getText(), discussion.getDiscussionId());
      textArea.setText("");
      refresh.run();
    });

    return dialogPanel;
  }

  private JPanel createNewCommentChainPanel(MergeRequestDiscussion discussion) {
    var panelBuilder = FormBuilder.createFormBuilder();

    for (var reviewComment : discussion.getReviewComments()) {
      var label = getTextField(discussion.getDiscussionId(), reviewComment);
      panelBuilder = panelBuilder.addComponent(label);
    }

    resolveButton.addActionListener(actionEvent -> {
      onResolve.accept(discussion.getDiscussionId());
      refresh.run();
    });

    panelBuilder.addComponent(resolveButton);

    return panelBuilder.addComponentFillVertically(new JPanel(), 10).getPanel();
  }

  @NotNull
  private JPanel getTextField(String discussionId, ReviewComment reviewComment) {
    var comment = reviewComment.toString();
    var noteId = reviewComment.getNoteId();

    var fakeLabel = new JTextField(comment);
    fakeLabel.setEditable(false);
    fakeLabel.setBorder(null);
    fakeLabel.setBackground(null);
    fakeLabel.setPreferredSize(new Dimension(317, 50));

    var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panel.add(fakeLabel);
    var preferredSize = new Dimension(30, 30);

    var editButton = new JButton("E");
    editButton.addActionListener(l -> {
      onEditComment.consume(textArea.getText(), discussionId, noteId);
      fakeLabel.setText(textArea.getText());
      textArea.setText("");
      refresh.run();
    });
    editButton.setPreferredSize(preferredSize);
    panel.add(editButton);

    var deleteButton = new JButton("X");
    deleteButton.addActionListener(l -> {
      onDeleteComment.accept(discussionId, noteId);
      panel.getParent().remove(panel);
      refresh.run();
    });
    deleteButton.setPreferredSize(preferredSize);
    panel.add(deleteButton);

    return panel;
  }
}
