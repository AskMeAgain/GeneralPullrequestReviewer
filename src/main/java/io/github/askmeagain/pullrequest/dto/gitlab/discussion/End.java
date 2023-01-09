package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class End {
  String line_code;
  String type;
  Integer old_line;
  Integer new_line;
}