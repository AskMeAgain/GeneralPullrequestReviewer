package io.github.askmeagain.pullrequest.dto.gitlab.diffslegacy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class References {
  @JsonProperty("short")
  String myshort;
  String relative;
  String full;
}
