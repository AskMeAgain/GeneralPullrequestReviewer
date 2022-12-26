package io.github.askmeagain.pullrequest.dto.github.mergerequest;

import lombok.Data;

@Data
public class Base {
  String label;
  String ref;
  String sha;
  User user;
  Repo repo;
}

