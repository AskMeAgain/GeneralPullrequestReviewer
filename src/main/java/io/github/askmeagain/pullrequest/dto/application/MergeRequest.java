package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Singular;
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

  @Singular
  List<ReviewFile> files;

  @Override
  public String toString() {
    return name;
  }
}
