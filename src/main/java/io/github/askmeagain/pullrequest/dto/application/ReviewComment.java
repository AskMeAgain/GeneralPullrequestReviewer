package io.github.askmeagain.pullrequest.dto.application;

import com.intellij.openapi.util.TextRange;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReviewComment {

  Integer line;

  String text;

}