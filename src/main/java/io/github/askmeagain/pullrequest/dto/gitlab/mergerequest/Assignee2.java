package io.github.askmeagain.pullrequest.dto.gitlab.mergerequest;

import lombok.Data;

@Data
public class Assignee2 {
  public String name;
  public String username;
  public int id;
  public String state;
  public String avatar_url;
  public String web_url;
}
