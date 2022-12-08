package io.github.askmeagain.pullrequest.gui.diffviewer;

import com.intellij.diff.editor.ChainDiffVirtualFile;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DiffEditorProvider implements FileEditorProvider {

  @Override
  public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
    return false;
    //return file instanceof ChainDiffVirtualFile;
  }

  @Override
  public @NotNull FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
    //return new DiffEditor(file);
    return null;
  }

  @Override
  public @NotNull @NonNls String getEditorTypeId() {
    return "Super_Unique_Editor_Id_DIff_Manager";
  }

  @Override
  public @NotNull FileEditorPolicy getPolicy() {
    return FileEditorPolicy.NONE;
  }
}
