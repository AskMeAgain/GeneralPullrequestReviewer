package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MergeRequestDiscussion {

  String discussionId;

  int line;

  @Singular
  List<ReviewComment> reviewComments;

  boolean isSourceDiscussion;

}
