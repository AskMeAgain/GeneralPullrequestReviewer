package io.github.askmeagain.pullrequest.dto.github.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class GithubProjectResponse {
  int id;
  String node_id;
  String name;
  String full_name;
  @JsonProperty("private")
  boolean myprivate;
  Owner owner;
  String html_url;
  Object description;
  boolean fork;
  String url;
  String forks_url;
  String keys_url;
  String collaborators_url;
  String teams_url;
  String hooks_url;
  String issue_events_url;
  String events_url;
  String assignees_url;
  String branches_url;
  String tags_url;
  String blobs_url;
  String git_tags_url;
  String git_refs_url;
  String trees_url;
  String statuses_url;
  String languages_url;
  String stargazers_url;
  String contributors_url;
  String subscribers_url;
  String subscription_url;
  String commits_url;
  String git_commits_url;
  String comments_url;
  String issue_comment_url;
  String contents_url;
  String compare_url;
  String merges_url;
  String archive_url;
  String downloads_url;
  String issues_url;
  String pulls_url;
  String milestones_url;
  String notifications_url;
  String labels_url;
  String releases_url;
  String deployments_url;
  Date created_at;
  Date updated_at;
  Date pushed_at;
  String git_url;
  String ssh_url;
  String clone_url;
  String svn_url;
  Object homepage;
  int size;
  int stargazers_count;
  int watchers_count;
  String language;
  boolean has_issues;
  boolean has_projects;
  boolean has_downloads;
  boolean has_wiki;
  boolean has_pages;
  boolean has_discussions;
  int forks_count;
  Object mirror_url;
  boolean archived;
  boolean disabled;
  int open_issues_count;
  Object license;
  boolean allow_forking;
  boolean is_template;
  boolean web_commit_signoff_required;
  ArrayList<Object> topics;
  String visibility;
  int forks;
  int open_issues;
  int watchers;
  String default_branch;
  Permissions permissions;
  boolean allow_squash_merge;
  boolean allow_merge_commit;
  boolean allow_rebase_merge;
  boolean allow_auto_merge;
  boolean delete_branch_on_merge;
  boolean allow_update_branch;
  boolean use_squash_pr_title_as_default;
  String squash_merge_commit_message;
  String squash_merge_commit_title;
  String merge_commit_message;
  String merge_commit_title;
  int network_count;
  int subscribers_count;
}
