package io.github.askmeagain.pullrequest.nodes.test;

import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.nodes.interfaces.ProjectMarker;
import io.github.askmeagain.pullrequest.services.vcs.test.TestService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestProjectNode extends BaseTreeNode implements ProjectMarker {
  @Getter
  private final String url = "http://google.de";
  private final String projectName;
  private final TestService testService = TestService.getInstance();

  @Override
  public String toString() {
    return String.format("%s", projectName);
  }

  @Override
  public void onCreation() {
    testService.getMergeRequests(null, null)
        .stream()
        .map(TestMergeRequestNode::new)
        .forEach(this::add);
  }

}
