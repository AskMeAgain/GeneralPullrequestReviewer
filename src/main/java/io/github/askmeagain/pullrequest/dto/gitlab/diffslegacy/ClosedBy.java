package io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy;

import lombok.Data;

@Data
public class ClosedBy {
  int id;
  String username;
  String name;
  String state;
  String avatar_url;
  String web_url;
}
