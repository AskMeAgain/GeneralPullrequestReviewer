package io.github.askmeagain.pullrequest.dto.application;

import com.intellij.openapi.util.Key;

import java.util.List;

public class TransferKey {
  public static final Key<String> SourceBranch = Key.create("sourceBranch");
  public static final Key<String> TargetBranch = Key.create("targetBranch");
  public static final Key<ReviewFile> DataContextKeySource = Key.create("source");
  public static final Key<ReviewFile> DataContextKeyTarget = Key.create("target");
  public static final Key<Boolean> IsSource = Key.create("isSource");
  public static final Key<String> MergeRequestId = Key.create("mergeRequestId");
  public static final Key<String> ProjectId = Key.create("projectId");
  public static final Key<String> FileName = Key.create("fileName");
  public static final Key<ConnectionConfig> Connection = Key.create("Connection");
  public static final Key<String> CommitId = Key.create("CommitId");
  public static final Key<Integer> HunkStart = Key.create("HunkStart");
  public static final Key<Integer> hunkEnd = Key.create("HunkEnd");
  public static final Key<List<MergeRequestDiscussion>> AllDiscussions = Key.create("allDiscussions");
}
