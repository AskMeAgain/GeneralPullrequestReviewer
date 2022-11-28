package io.github.askmeagain.pullrequest.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ReviewFile {

  String fileName;
  String fileContent;
  @Singular
  List<ReviewComment> reviewComments;
}
