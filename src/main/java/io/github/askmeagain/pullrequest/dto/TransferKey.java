package io.github.askmeagain.pullrequest.dto;

import com.intellij.openapi.util.Key;
import io.github.askmeagain.pullrequest.dto.application.MergeRequestDiscussion;
import io.github.askmeagain.pullrequest.dto.application.ReviewFile;

import java.util.List;

public class TransferKey {
  public static final Key<ReviewFile> DataContextKeySource = Key.create("source");
  public static final Key<ReviewFile> DataContextKeyTarget = Key.create("target");
  public static final Key<Boolean> IsSource = Key.create("isSource");
  public static final Key<String> MergeRequestId = Key.create("mergeRequestId");
  public static final Key<String> FileName = Key.create("fileName");
  public static final Key<String> ConnectionName = Key.create("ConnectionName");
  public static final Key<List<MergeRequestDiscussion>> AllDiscussions = Key.create("allDiscussions");
}
