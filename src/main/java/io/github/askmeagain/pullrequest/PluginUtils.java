package io.github.askmeagain.pullrequest;

import java.awt.*;

public class PluginUtils {

  public static String toHexColor(Color col) {
    return "#" + Integer.toHexString((col.getRGB() & 0xffffff) | 0x1000000).substring(1);
  }

  public static String encodePath(String path) {
    return path
        .replaceAll("\\.", "%2E")
        .replaceAll("/", "%2F")
        .replaceAll(" ", "%20")
        .replaceAll("-", "%2D");
  }
}
