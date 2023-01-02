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

  Integer firstHunk;
  Integer lastHunk;

  public boolean isWithinReach() {
    if (firstHunk == null || lastHunk == null) {
      return true;
    }
    return firstHunk <= lineStart && lastHunk <= lineEnd;
  }
}
