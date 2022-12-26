package io.github.askmeagain.pullrequest.dto.github;

import lombok.Data;

@Data
public class Head {
  String label;
  String ref;
  String sha;
  User user;
  Repo repo;
}
