package io.github.askmeagain.pullrequest.dto;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PullrequestPluginState {

  VcsImplementation selectedVcsImplementation = VcsImplementation.GITLAB;
  String gitlabUrl;

  public String getGitlabToken() {
    var credentialAttributes = createCredentialAttributes();
    return PasswordSafe.getInstance().getPassword(credentialAttributes);
  }

  public void setGitlabToken(String token) {
    var credentials = new Credentials("-", token);
    PasswordSafe.getInstance().set(createCredentialAttributes(), credentials);
  }

  private CredentialAttributes createCredentialAttributes() {
    return new CredentialAttributes(CredentialAttributesKt.generateServiceName("PullrequestPlugin", "gitlabToken"));
  }
}