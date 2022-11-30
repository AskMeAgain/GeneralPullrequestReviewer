package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
public class End{
    String line_code;
    String type;
}