package io.github.askmeagain.pullrequest.dto.application;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import io.github.askmeagain.pullrequest.settings.ConnectionConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class PullrequestPluginState {

  private List<ConnectionConfig> connectionConfigs = new ArrayList<>();

  public Map<String, ConnectionConfig> getMap(){
    return connectionConfigs.stream()
        .collect(Collectors.toMap(x -> x.getName(), x -> x));
  }

//  public String getGitlabToken() {
//    var credentialAttributes = createCredentialAttributes();
//    return PasswordSafe.getInstance().getPassword(credentialAttributes);
//  }
//
//  public void setGitlabToken(String token) {
//    var credentials = new Credentials("-", token);
//    PasswordSafe.getInstance().set(createCredentialAttributes(), credentials);
//  }
//
//  private CredentialAttributes createCredentialAttributes() {
//    return new CredentialAttributes(CredentialAttributesKt.generateServiceName("PullrequestPlugin", "gitlabToken"));
//  }
}