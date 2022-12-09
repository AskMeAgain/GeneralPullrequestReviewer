package io.github.askmeagain.pullrequest.services;

import java.awt.*;

public class PluginUtils {

  public static String toHexColor(Color col) {
    return "#" + Integer.toHexString((col.getRGB() & 0xffffff) | 0x1000000).substring(1);
  }
}
