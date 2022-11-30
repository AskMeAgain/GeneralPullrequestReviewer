package io.github.askmeagain.pullrequest.services.vcs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import io.github.askmeagain.pullrequest.dto.application.MergeRequest;
import io.github.askmeagain.pullrequest.dto.application.PullrequestPluginState;
import io.github.askmeagain.pullrequest.dto.application.ReviewComment;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;
import io.github.askmeagain.pullrequest.dto.gitlab.discussion.GitlabDiscussionResponse;
import io.github.askmeagain.pullrequest.dto.gitlab.mergerequest.GitlabMergeRequestResponse;
import io.github.askmeagain.pullrequest.services.PluginManagementService;
import io.github.askmeagain.pullrequest.settings.PersistenceManagementService;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;

@Service
public final class GitlabService implements VcsService {

  @Getter(lazy = true)
  private final PullrequestPluginState state = PersistenceManagementService.getInstance().getState();

  public static GitlabService getInstance() {
    return ApplicationManager.getApplication().getService(GitlabService.class);
  }

  @SneakyThrows
  public List<MergeRequest> getMergeRequests() {
    return getGitlabMergeRequests().stream()
        .map(pr -> MergeRequest.builder()
            .name(pr.getTitle())
            .build())
        .collect(Collectors.toList());
  }

  @SneakyThrows
  private List<GitlabDiscussionResponse> getDiscussionsOfPr(){
    //GET /projects/:id/merge_requests/:merge_request_iid/discussions
    var exampleString = getReadString("gitlab-discussion-example.json");
    var actualResponse = new ObjectMapper().readValue(exampleString, GitlabDiscussionResponse.class);
    return List.of(actualResponse);
  }

  @SneakyThrows
  private List<GitlabMergeRequestResponse> getGitlabMergeRequests(){
    //GET /merge_requests?state=opened
    var exampleString = getReadString("gitlab-mergerequest-example.json");
    var actualResponse = new ObjectMapper().readValue(exampleString, GitlabMergeRequestResponse.class);
    return List.of(actualResponse);
  }

  @SneakyThrows
  private static String getReadString(String path) {
    return new String(PluginManagementService.class.getClassLoader().getResourceAsStream(path).readAllBytes());
  }
}
