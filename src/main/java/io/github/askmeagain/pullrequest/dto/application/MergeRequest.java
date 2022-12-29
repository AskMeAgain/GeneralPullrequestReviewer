package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MergeRequest {

  String name;
  String id;

  String sourceBranch;
  String targetBranch;

  Boolean approved;

  List<String> reviewer;

  String commitSha;

  @Override
  public String toString() {
    return name;
  }
}
