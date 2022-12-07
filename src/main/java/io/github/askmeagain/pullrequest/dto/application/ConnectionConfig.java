package io.github.askmeagain.pullrequest.dto.application;

import com.intellij.util.Producer;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionConfig {

  VcsImplementation vcsImplementation;
  String name;
  Map<String, String> configs = new HashMap<>();

  public ConnectionConfig(String name) {
    this.name = name;
  }

  private Runnable refresh;

  @com.intellij.util.xmlb.annotations.Transient
  public Runnable getRefresh() {
    return this.refresh;
  }

}
