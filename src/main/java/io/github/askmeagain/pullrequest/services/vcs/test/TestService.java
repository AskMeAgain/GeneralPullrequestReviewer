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
    return List.of(MergeRequest.builder()
            .targetBranch("master")
            .sourceBranch("target")
            .id("1")
            .name("The first test PR")
            .files(List.of(
                ReviewFile.builder()
                    .fileName("File1.txt")
                    .fileContent("abcasd")
                    .build(),
                ReviewFile.builder()
                    .fileName("File2.txt")
                    .fileContent("abcasd")
                    .build()
            ))
            .build(),
        MergeRequest.builder()
            .targetBranch("master")
            .sourceBranch("target2")
            .id("2")
            .name("The second test PR")
            .files(List.of(
                ReviewFile.builder()
                    .fileName("File3.txt")
                    .fileContent("abcasd")
                    .build(),
                ReviewFile.builder()
                    .fileName("File4.txt")
                    .fileContent("abcasd")
                    .build()))
            .build()
    );
  }

  @Override
  public void addCommentToThread(String projectId, ConnectionConfig connectionName, String mergeRequestId, String discussionId, GitlabAddCommentToDiscussionRequest request) {
    //Do nothing
  }

  @Override
  public List<String> getFilesOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId) {
    return List.of("File1.txt", "File2.txt");
  }

  @Override
  public List<MergeRequestDiscussion> getCommentsOfPr(String projectId, ConnectionConfig connectionName, String mergeRequestId) {
    return List.of(
        MergeRequestDiscussion.builder()
            .line(10)
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
            .line(10)
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
  public String getFileOfBranch(String projectId, ConnectionConfig connectionName, String branch, String filePath) {
    return "Abc\nasd\nasd\nasd\nasd\nasddddd\nghjghjhgj\nfghfghfgh\nasdkfgk";
  }

  @Override
  public void addMergeRequestComment(String projectId, ConnectionConfig connectionName, String mergeRequestId, CommentRequest comment) {
    //Do nothing
  }
}
