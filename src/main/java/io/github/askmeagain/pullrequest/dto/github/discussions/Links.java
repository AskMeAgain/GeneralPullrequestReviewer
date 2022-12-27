package io.github.askmeagain.pullrequest.dto.github.discussions;

import lombok.Data;

@Data
public class Links {
  Self self;
  Html html;
  PullRequest pull_request;
}
