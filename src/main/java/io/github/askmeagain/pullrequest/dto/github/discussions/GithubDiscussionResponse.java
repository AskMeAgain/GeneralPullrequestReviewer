package io.github.askmeagain.pullrequest.dto.github.discussions;

import lombok.Data;

import java.util.Date;

@Data
public class GithubDiscussionResponse {
  String url;
  int pull_request_review_id;
  int id;
  String node_id;
  String diff_hunk;
  String path;
  int position;
  int original_position;
  String commit_id;
  String original_commit_id;
  User user;
  String body;
  Date created_at;
  Date updated_at;
  String html_url;
  String pull_request_url;
  String author_association;
  Links _links;
  Reactions reactions;
  Object start_line;
  Object original_start_line;
  Object start_side;
  int line;
  int original_line;
  String side;
  String in_reply_to_id;
}
