package io.github.askmeagain.pullrequest.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MergeRequest {

  String name;

  String sourceBranch;
  String targetBranch;

  @Singular
  List<ReviewFile> files;

  @Override
  public String toString(){
    return name;
  }
}
