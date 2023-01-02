package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommentRequest {

  boolean sourceComment;
  String text;
  Integer lineStart;
  Integer lineEnd;

  String commitId;
  String newFileName;
  String oldFileName;
}
