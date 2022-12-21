package io.github.askmeagain.pullrequest.dto.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ConnectionConfig {

  VcsImplementation vcsImplementation;
  String name;
  Map<String, String> configs = new HashMap<>();

  public ConnectionConfig(VcsImplementation vcsImplementation) {
    this.vcsImplementation = vcsImplementation;
  }

}
