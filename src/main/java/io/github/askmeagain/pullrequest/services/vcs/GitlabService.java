package io.github.askmeagain.pullrequest.services.vcs;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.MergeRequest;
import io.github.askmeagain.pullrequest.dto.PullrequestPluginState;
import io.github.askmeagain.pullrequest.dto.ReviewComment;
import io.github.askmeagain.pullrequest.dto.ReviewFile;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import io.github.askmeagain.pullrequest.settings.PersistenceManagementService;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;

@Service
public final class GitlabService implements VcsService {

  @Getter(lazy = true)
  private final PullrequestPluginState state = PersistenceManagementService.getInstance().getState();

  public static GitlabService getInstance() {
    return ApplicationManager.getApplication().getService(GitlabService.class);
  }

  public List<MergeRequest> getMergeRequests() {
    //this is how you get the PW/url:
    var url = getState().getGitlabUrl();
    var token = getState().getGitlabUrl();

    return LIST;
  }

  @SneakyThrows
  private static String getReadString(String path) {
    return new String(PluginManagementService.class.getClassLoader().getResourceAsStream(path).readAllBytes());
  }

  private static final List<MergeRequest> LIST = List.of(
      MergeRequest.builder()
          .name("first PR")
          .sourceBranch("source Branch")
          .targetBranch("target Branch")
          .file(ReviewFile.builder()
              .reviewComments(List.of(
                  ReviewComment.builder()
                      .text("import com.intellij.openapi.editor.markup.HighlighterLayer;")
                      .line(10)
                      .build(),
                  ReviewComment.builder()
                      .text("import io.github.askmeagain.pullrequest.dto.ReviewComment;")
                      .line(15)
                      .build(),
                  ReviewComment.builder()
                      .text("import java.util.List;")
                      .line(20)
                      .build(),
                  ReviewComment.builder()
                      .text("@Getter(lazy = true)")
                      .line(24)
                      .build()))
              .fileContent(getReadString("file1.txt"))
              .fileName("file1.txt")
              .build())
          .build(),
      MergeRequest.builder()
          .name("another PR")
          .sourceBranch("source Branch 2")
          .targetBranch("target Branch 2")
          .file(ReviewFile.builder()
              .reviewComments(List.of(
                  ReviewComment.builder()
                      .text("third item")
                      .line(23)
                      .build(), ReviewComment.builder()
                      .text("fourth item")
                      .line(40)
                      .build()))
              .fileContent(getReadString("file2.txt"))
              .fileName("file2.txt")
              .build())
          .build());
}
