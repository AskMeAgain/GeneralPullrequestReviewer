package io.github.askmeagain.pullrequest.dto.gitlab.mergerequest;

import lombok.Data;

@Data
public class Reviewer {
  public int id;
  public String name;
  public String username;
  public String state;
  public String avatar_url;
  public String web_url;
}
