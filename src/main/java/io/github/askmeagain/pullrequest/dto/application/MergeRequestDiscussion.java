package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
public class MergeRequestDiscussion {

  @EqualsAndHashCode.Include
  String discussionId;

  int startLine;
  int endLine;
  String url;
  Boolean resolved;

  @Singular
  List<ReviewComment> reviewComments;

  boolean isSourceDiscussion;

}
