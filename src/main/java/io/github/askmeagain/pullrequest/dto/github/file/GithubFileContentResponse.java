package io.github.askmeagain.pullrequest.dto.github.file;

import lombok.Data;

@Data
public class GithubFileContentResponse {
  String name;
  String path;
  String sha;
  int size;
  String url;
  String html_url;
  String git_url;
  String download_url;
  String type;
  String content;
  String encoding;
  Links _links;
}
