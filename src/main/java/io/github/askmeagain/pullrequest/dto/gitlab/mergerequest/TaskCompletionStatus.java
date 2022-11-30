package io.github.askmeagain.pullrequest.dto.gitlab.mergerequest;

import lombok.Data;

@Data
public class TaskCompletionStatus {
  public int count;
  public int completed_count;
}
