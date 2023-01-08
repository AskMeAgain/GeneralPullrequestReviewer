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

  DiffHunk hunk;

  public boolean isWithinReach() {
    if (firstHunk == null || lastHunk == null) {
      return true;
    }
    return firstHunk <= lineStart && lastHunk <= lineEnd;
  }
}
