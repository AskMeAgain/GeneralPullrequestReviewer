package io.github.askmeagain.pullrequest;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.util.TextRange;
import io.github.askmeagain.pullrequest.dto.MergeRequest;
import io.github.askmeagain.pullrequest.dto.ReviewComment;
import lombok.Getter;

import javax.swing.*;
import java.util.List;

@Service
public final class PullrequestService {

  @Getter
  private final DefaultListModel<String> defaultListModelString = new DefaultListModel<>();

  private static final List<MergeRequest> LIST = List.of(
      MergeRequest.builder()
          .name("name1")
          .files(List.of("asd"))
          .reviewComments(List.of(
              ReviewComment.builder()
                  .text("first item")
                  .textRange(new TextRange(10, 23))
                  .build(), ReviewComment.builder()
                  .text("second item")
                  .textRange(new TextRange(67, 74))
                  .build()))
          .build(),
      MergeRequest.builder()
          .name("another PR")
          .files(List.of("123233asdasd"))
          .reviewComments(List.of(
              ReviewComment.builder()
                  .text("third item")
                  .textRange(new TextRange(10, 23))
                  .build(), ReviewComment.builder()
                  .text("fourth item")
                  .textRange(new TextRange(67, 74))
                  .build()))
          .build());

  public List<MergeRequest> getMergeRequests() {
    return LIST;
  }

  public void refreshList() {
    //TODO
    System.out.println("Refresh list!");

    defaultListModelString.clear();
    LIST.forEach(x -> defaultListModelString.addElement(x.getName()));

  }

  public static PullrequestService getInstance() {
    return ApplicationManager.getApplication().getService(PullrequestService.class);
  }

}
