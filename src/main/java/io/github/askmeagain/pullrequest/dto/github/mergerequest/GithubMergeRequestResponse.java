package io.github.askmeagain.pullrequest.dto.github.mergerequest;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class GithubMergeRequestResponse {
  String url;
  int id;
  String node_id;
  String html_url;
  String diff_url;
  String patch_url;
  String issue_url;
  int number;
  String state;
  boolean locked;
  String title;
  User user;
  Object body;
  Date created_at;
  Date updated_at;
  Object closed_at;
  Object merged_at;
  String merge_commit_sha;
  Object assignee;
  ArrayList<Assignee> assignees;
  ArrayList<Object> requested_reviewers;
  ArrayList<Object> requested_teams;
  ArrayList<Object> labels;
  Object milestone;
  boolean draft;
  String commits_url;
  String review_comments_url;
  String review_comment_url;
  String comments_url;
  String statuses_url;
  Head head;
  Base base;
  Links _links;
  String author_association;
  Object auto_merge;
  Object active_lock_reason;
}
