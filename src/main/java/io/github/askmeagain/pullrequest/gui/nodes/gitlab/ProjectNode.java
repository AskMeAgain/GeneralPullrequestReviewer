package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
public class ProjectNode {
  private final String project;

  @Override
  public String toString() {
    return project;
  }
}
