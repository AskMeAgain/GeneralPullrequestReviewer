package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProjectResponse {
  public String name;
}
