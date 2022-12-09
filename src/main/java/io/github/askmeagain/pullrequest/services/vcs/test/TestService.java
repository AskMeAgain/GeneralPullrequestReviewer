package io.github.askmeagain.pullrequest.services.vcs.test;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.dto.gitlab.discussionnote.GitlabAddCommentToDiscussionRequest;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;

import java.util.List;

@Service
public final class TestService implements VcsService {

  public static TestService getInstance() {
    return ApplicationManager.getApplication().getService(TestService.class);
  }

  @Override
  public List<MergeRequest> getMergeRequests(String projectId, ConnectionConfig connectionName) {
    //TODO
    return List.of(MergeRequest.builder()
        .targetBranch("target")
        .sourceBranch("master")
        .id("1")
        .name("The first test PR")
        .files(List.of(
            ReviewFile.builder()
                .fileName("File1.txt")
                .fileContent("abcasd")
                .build()))
        .build());
  }

  @Override
  public void addCommentToThread(String projectId, ConnectionConfig connectionName, String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request) {

  }

  @Override
  public List<String> getFilesOfPr(String projectId,ConnectionConfig connectionName, String mergeRequestId) {
    return null;
  }

  @Override
  public List<MergeRequestDiscussion> getCommentsOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId) {
    return null;
  }

  @Override
  public String getFileOfBranch(String projectId, ConnectionConfig connectionName, String branch, String filePath) {
    return null;
  }

  @Override
  public void addMergeRequestComment(String projectId, ConnectionConfig connectionName, String mergeRequestId, CommentRequest comment) {

  }
}
