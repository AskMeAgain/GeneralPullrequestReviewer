package io.github.askmeagain.pullrequest.dto.application;

import com.intellij.ui.SimpleTextAttributes;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.askmeagain.pullrequest.PluginUtils.toHexColor;

@Data
public class PullrequestPluginState {

  private List<ConnectionConfig> connectionConfigs = new ArrayList<>();

  public Map<String, ConnectionConfig> getMap(){
    return connectionConfigs.stream()
        .collect(Collectors.toMap(ConnectionConfig::getName, Function.identity()));
  }

  String mergeRequestColor = toHexColor(SimpleTextAttributes.DARK_TEXT.getFgColor());
  String discussionTextColor = toHexColor(SimpleTextAttributes.DARK_TEXT.getFgColor());
  String mergeRequestCommentHint = toHexColor(SimpleTextAttributes.DARK_TEXT.getFgColor());
  String fileColor = toHexColor(SimpleTextAttributes.DARK_TEXT.getFgColor());
}