package io.github.askmeagain.pullrequest.dto.gitlab.project;

import lombok.Data;

@Data
public class Permissions {
  public ProjectAccess project_access;
  public GroupAccess group_access;
}
