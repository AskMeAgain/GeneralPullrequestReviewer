package io.github.askmeagain.pullrequest.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MergeRequest {

  String name;
  List<String> files;
  List<ReviewComment> reviewComments;
}
