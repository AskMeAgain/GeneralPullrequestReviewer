package io.github.askmeagain.pullrequest.gui.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.FormBuilder;
import io.github.askmeagain.pullrequest.services.PersistenceManagementService;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Consumer;

public class AddCommentDialog extends DialogWrapper {

  private JTextArea textArea;
  private final AnActionEvent e;
  private Consumer<String> run;

  public AddCommentDialog(AnActionEvent e, Consumer<String> run) {
    super(true);

    this.e = e;
    this.run = run;

    setTitle("Count Settings");
    init();
  }

  @Override
  public JComponent getPreferredFocusedComponent() {
    return textArea;
  }

  @Override
  public void doOKAction() {
    run.accept(textArea.getText());
    this.close(0);
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {

    textArea = new JTextArea(3, 10);

    return FormBuilder.createFormBuilder()
        .addLabeledComponent(new JBLabel("Text", JLabel.TRAILING), textArea, 1, true)
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();
  }
}