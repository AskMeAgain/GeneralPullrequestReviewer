package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewComment {

  String text;
  String author;
  String discussionId;
  String noteId;

  @Override
  public String toString() {
    return author + ": " + text;
  }

}
