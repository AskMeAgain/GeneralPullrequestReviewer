package io.github.askmeagain.pullrequest.dto;

import com.intellij.openapi.util.TextRange;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewComment {

  TextRange textRange;

  String text;

}
