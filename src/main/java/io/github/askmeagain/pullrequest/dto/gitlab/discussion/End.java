package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.Data;

@Data
public class End {
  String line_code;
  String type;
  Integer old_line;
  Integer new_line;
}