package io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy;

import lombok.Data;

@Data
public class Change {
  String diff;
  String new_path;
  String old_path;
  String a_mode;
  String b_mode;
  boolean new_file;
  boolean renamed_file;
  boolean deleted_file;
}
