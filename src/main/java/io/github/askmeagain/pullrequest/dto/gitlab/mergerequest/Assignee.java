package io.github.askmeagain.pullrequest.dto.gitlab.mergerequest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class Assignee{
    public int id;
    public String name;
    public String username;
    public String state;
    public Object avatar_url;
    public String web_url;
}

