package io.github.askmeagain.pullrequest.dto.gitlab.comment;

import io.github.askmeagain.pullrequest.dto.gitlab.discussion.Position;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GitlabMergeRequestCommentRequest {

  Position position;
  String body;

}
