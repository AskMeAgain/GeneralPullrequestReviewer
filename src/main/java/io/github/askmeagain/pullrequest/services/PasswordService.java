package io.github.askmeagain.pullrequest.services;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;

@Service
public final class PasswordService {

  public static PasswordService getInstance() {
    return ApplicationManager.getApplication().getService(PasswordService.class);
  }

  public void setPassword(String key, String password) {
    var credentials = new Credentials(key, password);
    PasswordSafe.getInstance().set(createCredentialAttributes(key), credentials);
  }

  public String getPassword(String key) {
    var credentialAttributes = createCredentialAttributes(key);
    return PasswordSafe.getInstance().getPassword(credentialAttributes);
  }

  private CredentialAttributes createCredentialAttributes(String key) {
    return new CredentialAttributes(CredentialAttributesKt.generateServiceName("PullrequestPlugin", key + "-password"));
  }
}
