package io.github.askmeagain.pullrequest.gui.nodes.gitlab;

import com.intellij.ui.treeStructure.Tree;
import io.github.askmeagain.pullrequest.dto.application.ConnectionConfig;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.gui.nodes.BaseTreeNode;
import io.github.askmeagain.pullrequest.services.vcs.gitlab.GitlabService;
import lombok.RequiredArgsConstructor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GitlabProjectNode extends BaseTreeNode {
  private final String projectId;
  private final ConnectionConfig connectionConfig;


  private final Tree tree;
  private final String projectName;
  private final GitlabService gitlabService = GitlabService.getInstance();

  @Override
  public String toString() {
    return String.format("%s (%s)", projectName, projectId);
  }

  @Override
  public void refresh() {
    var mergeRequests = gitlabService.getMergeRequests(connectionConfig);
    var mergeRequestsMap = mergeRequests.stream()
        .collect(Collectors.toMap(MergeRequest::getId, Function.identity()));

    var childMap = new HashMap<String, GitlabMergeRequestNode>();

    //check for existing
    for (int i = 0; i < getChildCount(); i++) {
      var child = getChildAt(i);
      if (child instanceof GitlabMergeRequestNode) {
        var casted = (GitlabMergeRequestNode) child;
        if (mergeRequestsMap.containsKey(casted.getMergeRequestId())) {
          casted.refresh();
          childMap.put(casted.getMergeRequestId(), casted);
        } else {
          remove(i);
          i--;
        }
      }
    }

    //new merge requests
    for (var mergeRequest : mergeRequests) {
      if (!childMap.containsKey(mergeRequest.getId())) {
        var newNode = new GitlabMergeRequestNode(mergeRequest, tree, connectionConfig);
        add(newNode);
      }
    }

    super.refresh();
  }

  @Override
  public void onCreation() {
    add(new DefaultMutableTreeNode("hidden"));
  }

  @Override
  public void beforeExpanded() {
    removeAllChildren();

    gitlabService.getMergeRequests(connectionConfig)
        .stream()
        .map(mergeRequest -> new GitlabMergeRequestNode(mergeRequest, tree, connectionConfig))
        .peek(GitlabMergeRequestNode::onCreation)
        .forEach(this::add);
  }
}
