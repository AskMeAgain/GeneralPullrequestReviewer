package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class MergeRequestDiscussion {

  String discussionId;
  int line;
  List<ReviewComment> reviewComments;
  boolean isSourceDiscussion;

}
