package io.github.askmeagain.pullrequest.dto.gitlab.project;

import lombok.Data;

@Data
public class Namespace {
  public int id;
  public String name;
  public String path;
  public String kind;
  public String full_path;
  public Object parent_id;
  public Object avatar_url;
  public String web_url;
}
