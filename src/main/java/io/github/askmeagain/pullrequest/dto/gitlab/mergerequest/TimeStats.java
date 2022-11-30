package io.github.askmeagain.pullrequest.dto.gitlab.mergerequest;

import lombok.Data;

@Data
public class TimeStats {
  public int time_estimate;
  public int total_time_spent;
  public Object human_time_estimate;
  public Object human_total_time_spent;
}
