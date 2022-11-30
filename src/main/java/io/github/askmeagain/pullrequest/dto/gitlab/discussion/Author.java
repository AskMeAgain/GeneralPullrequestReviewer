package io.github.askmeagain.pullrequest.dto.gitlab.discussion;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.ArrayList;

@Data
public class Author{
    int id;
    String name;
    String username;
    String state;
    String avatar_url;
    String web_url;
}













