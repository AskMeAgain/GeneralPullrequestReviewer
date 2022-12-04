package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position {
  String base_sha;
  String start_sha;
  String head_sha;
  String old_path;
  String new_path;
  String position_type;
  Integer old_line;
  Integer new_line;
  LineRange line_range;
}