package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MergeRequest {

  String name;
  String id;

  String sourceBranch;
  String targetBranch;

  Boolean approved;

  @Override
  public String toString() {
    return name;
  }
}
