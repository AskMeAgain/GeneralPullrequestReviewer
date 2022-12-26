package io.github.askmeagain.pullrequest.dto.github.diffs;


import lombok.Data;

@Data
public class GithubDiffResponse {
  String sha;
  String filename;
  String status;
  int additions;
  int deletions;
  int changes;
  String blob_url;
  String raw_url;
  String contents_url;
  String patch;
}

