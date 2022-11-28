package io.github.askmeagain.pullrequest;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.util.TextRange;
import io.github.askmeagain.pullrequest.dto.MergeRequest;
import io.github.askmeagain.pullrequest.dto.ReviewComment;
import io.github.askmeagain.pullrequest.dto.ReviewFile;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public final class PullrequestService {

  @Getter
  private final DefaultListModel<MergeRequest> defaultListModelString = new DefaultListModel<>();

  private static final List<MergeRequest> LIST = List.of(
      MergeRequest.builder()
          .name("first PR")
          .file(ReviewFile.builder()
              .reviewComments(List.of(
                  ReviewComment.builder()
                      .text("third item")
                      .textRange(new TextRange(10, 23))
                      .build(), ReviewComment.builder()
                      .text("fourth item")
                      .textRange(new TextRange(67, 74))
                      .build(),
                  ReviewComment.builder()
                      .text("third item")
                      .textRange(new TextRange(10, 23))
                      .build(), ReviewComment.builder()
                      .text("fourth item")
                      .textRange(new TextRange(67, 74))
                      .build()))
              .fileContent(getReadString("file1.txt"))
              .fileName("file1.txt")
              .build())
          .build(),
      MergeRequest.builder()
          .name("another PR")
          .file(ReviewFile.builder()
              .reviewComments(List.of(
                  ReviewComment.builder()
                      .text("third item")
                      .textRange(new TextRange(10, 23))
                      .build(), ReviewComment.builder()
                      .text("fourth item")
                      .textRange(new TextRange(67, 74))
                      .build()))
              .fileContent(getReadString("file2.txt"))
              .fileName("file2.txt")
              .build())
          .build());

  @SneakyThrows
  private static String getReadString(String path) {
    return new String(PullrequestService.class.getClassLoader().getResourceAsStream(path).readAllBytes());
  }

  public List<MergeRequest> getMergeRequests() {
    return LIST;
  }

  public void refreshList() {
    //TODO
    System.out.println("Refresh list!");

    defaultListModelString.clear();

    LIST.forEach(defaultListModelString::addElement);
  }

  public static PullrequestService getInstance() {
    return ApplicationManager.getApplication().getService(PullrequestService.class);
  }

}
