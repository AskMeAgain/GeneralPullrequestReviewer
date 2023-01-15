package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class MergeRequestDiscussion {

  String discussionId;

  int startLine;
  int endLine;
  String url;

  @Singular
  List<ReviewComment> reviewComments;

  boolean isSourceDiscussion;

}
