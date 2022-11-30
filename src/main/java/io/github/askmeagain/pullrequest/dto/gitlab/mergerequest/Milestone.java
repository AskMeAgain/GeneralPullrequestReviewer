package io.github.askmeagain.pullrequest.dto.gitlab.mergerequest;

import lombok.Data;

import java.util.Date;

@Data
public class Milestone {
  public int id;
  public int iid;
  public int project_id;
  public String title;
  public String description;
  public String state;
  public Date created_at;
  public Date updated_at;
  public String due_date;
  public String start_date;
  public String web_url;
}
