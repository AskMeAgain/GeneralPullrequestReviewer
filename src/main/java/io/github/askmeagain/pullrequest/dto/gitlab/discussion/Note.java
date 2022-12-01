package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.Data;

import java.util.Date;

@Data
public class Note {
  int id;
  String type;
  String body;
  Object attachment;
  Author author;
  Date created_at;
  Date updated_at;
  boolean system;
  int noteable_id;
  String noteable_type;
  Object noteable_iid;
  String commit_id;
  Position position;
  boolean resolved;
  boolean resolvable;
  Object resolved_by;
}