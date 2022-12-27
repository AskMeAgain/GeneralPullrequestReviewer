package io.github.askmeagain.pullrequest.dto.github.discussions;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Reactions {
  String url;
  int total_count;
  @JsonAlias("+1")
  int plusOne;
  @JsonAlias("-1")
  int minusOne;
  int laugh;
  int hooray;
  int confused;
  int heart;
  int rocket;
  int eyes;
}
