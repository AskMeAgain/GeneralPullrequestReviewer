package io.github.askmeagain.pullrequest.dto.gitlab.versions;

import lombok.Data;

import java.util.Date;

@Data
public class MergeRequestVersions {
  int id;
  String head_commit_sha;
  String base_commit_sha;
  String start_commit_sha;
  Date created_at;
  int merge_request_id;
  String state;
  String real_size;
}