package io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy;

import lombok.Data;

@Data
public class TaskCompletionStatus {
  int count;
  int completed_count;
}
