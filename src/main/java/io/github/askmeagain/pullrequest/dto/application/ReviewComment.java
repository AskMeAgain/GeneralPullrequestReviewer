package io.github.askmeagain.pullrequest.dto.application;

import com.intellij.openapi.util.TextRange;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewComment {

  Integer line;

  String text;
  boolean sourceComment;
  String author;
  String discussionId;

  @Override
  public String toString() {
    return author + ":" + text;
  }

}
