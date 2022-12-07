package io.github.askmeagain.pullrequest.dto.gitlab.project;

import lombok.Data;

@Data
public class Links {
  public String self;
  public String issues;
  public String merge_requests;
  public String repo_branches;
  public String labels;
  public String events;
  public String members;
  public String cluster_agents;
}
