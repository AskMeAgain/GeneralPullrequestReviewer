package io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy;

import lombok.Data;

@Data
public class TimeStats {
  int time_estimate;
  int total_time_spent;
  Object human_time_estimate;
  Object human_total_time_spent;
}
