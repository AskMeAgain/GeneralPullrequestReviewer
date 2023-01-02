package io.github.askmeagain.pullrequest.dto.github.comment;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GithubMergeRequestCommentRequest {

  String body;
  String path;
  String commit_id;
  String side;
  Integer line;
  Integer start_line;

}
