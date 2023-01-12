package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class End {
  String line_code;
  String type;
  Integer old_line;
  Integer new_line;
}