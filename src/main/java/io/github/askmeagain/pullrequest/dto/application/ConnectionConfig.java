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

  public ConnectionConfig(String name) {
    this.name = name;
  }

  private Runnable refresh;

  @com.intellij.util.xmlb.annotations.Transient
  public Runnable getRefresh() {
    return this.refresh;
  }


  @com.intellij.util.xmlb.annotations.Transient
  public String getPassword() {
    var credentialAttributes = createCredentialAttributes();
    return PasswordSafe.getInstance().getPassword(credentialAttributes);
  }

  public void setPassword(String password) {
    var credentials = new Credentials("-", password);
    PasswordSafe.getInstance().set(createCredentialAttributes(), credentials);
  }

  private CredentialAttributes createCredentialAttributes() {
    return new CredentialAttributes(CredentialAttributesKt.generateServiceName("PullrequestPlugin", name + "-password"));
  }
}
