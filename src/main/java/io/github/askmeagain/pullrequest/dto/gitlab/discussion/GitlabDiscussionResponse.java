package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.Data;

import java.util.ArrayList;

@Data
public class GitlabDiscussionResponse {
    String id;
    boolean individual_note;
    ArrayList<Note> notes;
}