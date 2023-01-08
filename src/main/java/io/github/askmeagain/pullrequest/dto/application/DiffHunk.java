package io.github.askmeagain.pullrequest.dto.application;


import lombok.Getter;

import java.util.regex.Pattern;

@Getter
public class DiffHunk {

  private String sourceFileName;
  private String targetFileName;

  private int firstLineSource;
  private int lastLineSource;
  private int firstLineTarget;
  private int lastLineTarget;

  public DiffHunk(String hunk) {

    var positionPattern = Pattern.compile("@@ (.*) @@");
    var namePattern = Pattern.compile("a/(.*) b/(.*) ");

    var matcher = positionPattern.matcher(hunk);
    if (matcher.find()) {

      //first hunk
      var group = matcher.group(0);
      var sourceTarget = group.replaceAll("@", "").trim().split(" ");
      var splitTarget = sourceTarget[0].split(",");
      var splitSource = sourceTarget[1].split(",");

      firstLineTarget = Integer.parseInt(splitTarget[0]) * -1;
      firstLineSource = Integer.parseInt(splitSource[0]);

      //last file hunk
      var group2 = matcher.group(matcher.groupCount() - 1);
      var sourceTarget2 = group2.replaceAll("@", "").trim().split(" ");
      var splitTarget2 = sourceTarget2[0].split(",");
      var splitSource2 = sourceTarget2[1].split(",");

      lastLineTarget = Integer.parseInt(splitTarget2[0]) * -1 + Integer.parseInt(splitTarget2[1]) - 1;
      lastLineSource = Integer.parseInt(splitSource2[0]) + Integer.parseInt(splitSource2[1]) - 1;

      //file names
    }
  }
}
