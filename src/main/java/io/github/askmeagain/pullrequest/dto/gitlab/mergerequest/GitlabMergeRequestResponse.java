package io.github.askmeagain.pullrequest.dto.gitlab.mergerequest;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class GitlabMergeRequestResponse {
  public int id;
  public int iid;
  public int project_id;
  public String title;
  public String description;
  public String state;
  public MergedBy merged_by;
  public MergeUser merge_user;
  public Date merged_at;
  public Object closed_by;
  public Object closed_at;
  public Date created_at;
  public Date updated_at;
  public String target_branch;
  public String source_branch;
  public int upvotes;
  public int downvotes;
  public Author author;
  public Assignee assignee;
  public ArrayList<Assignee> assignees;
  public ArrayList<Reviewer> reviewers;
  public int source_project_id;
  public int target_project_id;
  public ArrayList<String> labels;
  public boolean draft;
  public boolean work_in_progress;
  public Milestone milestone;
  public boolean merge_when_pipeline_succeeds;
  public String merge_status;
  public String detailed_merge_status;
  public String sha;
  public Object merge_commit_sha;
  public Object squash_commit_sha;
  public int user_notes_count;
  public Object discussion_locked;
  public boolean should_remove_source_branch;
  public boolean force_remove_source_branch;
  public boolean allow_collaboration;
  public boolean allow_maintainer_to_push;
  public String web_url;
  public References references;
  public TimeStats time_stats;
  public boolean squash;
  public TaskCompletionStatus task_completion_status;
}
