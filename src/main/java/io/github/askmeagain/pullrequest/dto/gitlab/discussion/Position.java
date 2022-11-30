package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.Data;

@Data
public class Position{
    String base_sha;
    String start_sha;
    String head_sha;
    String old_path;
    String new_path;
    String position_type;
    int old_line;
    int new_line;
    LineRange line_range;
}