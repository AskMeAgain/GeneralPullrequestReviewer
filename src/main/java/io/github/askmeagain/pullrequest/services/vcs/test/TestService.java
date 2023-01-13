package io.github.askmeagain.pullrequest.services.vcs.test;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.*;
import io.github.askmeagain.pullrequest.services.vcs.VcsService;

import java.util.List;

@Service
public final class TestService implements VcsService {

  public static TestService getInstance() {
    return ApplicationManager.getApplication().getService(TestService.class);
  }

  @Override
  public List<MergeRequest> getMergeRequests(String projectId, ConnectionConfig connectionName) {
    return List.of(MergeRequest.builder()
            .targetBranch("master")
            .sourceBranch("target")
            .id("1")
            .name("The first test PR")
            .build(),
        MergeRequest.builder()
            .targetBranch("master")
            .sourceBranch("target2")
            .id("2")
            .name("The second test PR")
            .build()
    );
  }

  @Override
  public void addCommentToThread(
      String projectId, ConnectionConfig connectionName,
      String mergeRequestId,
      String discussionId,
      String text
  ) {
    //Do nothing
  }

  @Override
  public void editComment(ConnectionConfig connectionName, String projectId, String mergeRequestId, String discussionId, String note_id, String body) {
  }

  @Override
  public void deleteComment(ConnectionConfig connectionName, String projectId, String mergeRequestId, String discussionId, String note_id) {
  }

  @Override
  public List<String> getFilesOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId) {
    return List.of("File1.txt", "File2.txt");
  }

  @Override
  public List<MergeRequestDiscussion> getCommentsOfPr(
      String projectId,
      ConnectionConfig connectionName,
      String mergeRequestId,
      String file
  ) {
    return List.of(
        MergeRequestDiscussion.builder()
            .startLine(10)
            .endLine(12)
            .isSourceDiscussion(true)
            .discussionId("1")
            .reviewComments(List.of(ReviewComment.builder()
                    .discussionId("1")
                    .author("TestPerson")
                    .text("abc")
                    .build(),
                ReviewComment.builder()
                    .discussionId("1")
                    .author("TestPerson")
                    .text("abc")
                    .build()))
            .build(),
        MergeRequestDiscussion.builder()
            .startLine(3)
            .endLine(4)
            .isSourceDiscussion(true)
            .discussionId("2")
            .reviewComments(List.of(ReviewComment.builder()
                    .discussionId("2")
                    .author("TestPerson")
                    .text("abc")
                    .build(),
                ReviewComment.builder()
                    .discussionId("2")
                    .author("TestPerson")
                    .text("abc")
                    .build()))
            .build()
    );
  }

  @Override
  public FileResponse getFileOfBranch(String projectId, ConnectionConfig connectionName, String branch, String filePath) {
    return FileResponse.builder()
        .fileContent("Abc\nasd\nasd\nasd\nasd\nasddddd\nghjghjhgj\nfghfghfgh\nasdkfgk")
        .commitId("abc+")
        .build();
  }

  @Override
  public void addMergeRequestComment(String projectId, ConnectionConfig connectionName, String mergeRequestId, CommentRequest comment) {
    //Do nothing
  }

  @Override
  public void approveMergeRequest(String projectId, ConnectionConfig connection, String mergeRequestId) {
    //Do nothing
  }

  @Override
  public ProjectResponse getProject(ConnectionConfig connection, String projectId) {
    throw new UnsupportedOperationException("unsupported");
  }
}
