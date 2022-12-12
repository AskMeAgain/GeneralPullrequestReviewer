package io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy;

import lombok.Data;

@Data
public class DiffRefs {
  String base_sha;
  String head_sha;
  String start_sha;
}
