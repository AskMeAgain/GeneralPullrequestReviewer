package io.github.askmeagain.pullrequest.dto.gitlab.project;

import lombok.Data;

@Data
public class GroupAccess {
  public int access_level;
  public int notification_level;
}
