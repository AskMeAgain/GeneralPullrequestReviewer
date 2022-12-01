package io.github.askmeagain.pullrequest.dto.gitlab.diffs;

import lombok.Data;

@Data
public class GitlabMergeRequestFileDiff {
  String old_path;
  String new_path;
  String a_mode;
  String b_mode;
  String diff;
  boolean new_file;
  boolean renamed_file;
  boolean deleted_file;
}

