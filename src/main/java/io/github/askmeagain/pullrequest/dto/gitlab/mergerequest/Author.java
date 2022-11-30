package io.github.askmeagain.pullrequest.dto.gitlab.mergerequest;

import lombok.Data;

@Data
public class Author {
  public int id;
  public String name;
  public String username;
  public String state;
  public Object avatar_url;
  public String web_url;
}
