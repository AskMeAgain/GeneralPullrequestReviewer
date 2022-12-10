package io.github.askmeagain.pullrequest.dto.application;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.util.Producer;
import io.github.askmeagain.pullrequest.dto.application.VcsImplementation;
import lombok.*;

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

}
