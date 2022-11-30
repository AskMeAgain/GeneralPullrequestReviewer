package io.github.askmeagain.pullrequest.services.vcs;

import io.github.askmeagain.pullrequest.dto.application.MergeRequest;

import java.util.List;

public interface VcsService {

  List<MergeRequest> getMergeRequests();

}
