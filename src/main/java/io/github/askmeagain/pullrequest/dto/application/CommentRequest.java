package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommentRequest {

  boolean sourceComment;
  String text;
  int line;

  String newFileName;
  String oldFileName;
}
