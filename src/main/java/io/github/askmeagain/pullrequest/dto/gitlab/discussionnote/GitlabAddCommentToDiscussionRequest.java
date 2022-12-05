package io.github.askmeagain.pullrequest.dto.gitlab.discussionnote;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GitlabAddCommentToDiscussionRequest {

  String body;

}
