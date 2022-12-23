package io.github.askmeagain.pullrequest.nodes.test;

import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.vcs.test.TestService;
import lombok.Getter;


public class TestMergeRequestNode extends BaseTreeNode {

  private final String display;
  @Getter
  private final String mergeRequestId;
  private final String sourceBranch;
  private final String targetBranch;

  public TestMergeRequestNode(MergeRequest mergeRequest) {
    display = mergeRequest.getName();
    mergeRequestId = mergeRequest.getId();
    sourceBranch = mergeRequest.getSourceBranch();
    targetBranch = mergeRequest.getTargetBranch();
  }

  private final TestService testService = TestService.getInstance();

  @Override
  public String toString() {
    return String.format("%s: %s", mergeRequestId, display);
  }

  @Override
  public void onCreation() {
    testService.getFilesOfPr(null, null, mergeRequestId)
        .stream()
        .map(file -> new TestFileNode(sourceBranch, targetBranch, file, mergeRequestId))
        .forEach(this::add);
  }
}