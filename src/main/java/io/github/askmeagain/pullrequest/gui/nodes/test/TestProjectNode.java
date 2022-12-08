package io.github.askmeagain.pullrequest.gui.nodes.test;

import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.vcs.test.TestService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestProjectNode extends BaseTreeNode {
  private final String projectName;
  private final TestService testService = TestService.getInstance();

  @Override
  public String toString() {
    return String.format("%s", projectName);
  }

  @Override
  public void onCreation() {
    testService.getMergeRequests(null)
        .stream()
        .map(TestMergeRequestNode::new)
        .forEach(this::add);
  }

}
