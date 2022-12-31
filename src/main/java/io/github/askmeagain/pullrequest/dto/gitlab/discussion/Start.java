package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Start{
    String line_code;
    String type;
}