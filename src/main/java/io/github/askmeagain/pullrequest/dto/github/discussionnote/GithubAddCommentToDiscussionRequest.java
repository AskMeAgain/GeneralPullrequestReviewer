package io.github.askmeagain.pullrequest.dto.github.discussionnote;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GithubAddCommentToDiscussionRequest {

  String body;

}
