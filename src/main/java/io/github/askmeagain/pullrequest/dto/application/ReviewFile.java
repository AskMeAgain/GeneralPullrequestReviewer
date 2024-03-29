package io.github.askmeagain.pullrequest.dto.application;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ReviewFile {

  String fileName;
  String branch;
  String fileContent;
  @Singular
  List<MergeRequestDiscussion> reviewDiscussions;
}
