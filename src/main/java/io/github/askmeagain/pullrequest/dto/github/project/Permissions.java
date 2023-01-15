package io.github.askmeagain.pullrequest.dto.github.project;

import lombok.Data;

@Data
public class Permissions {
  boolean admin;
  boolean maintain;
  boolean push;
  boolean triage;
  boolean pull;
}
