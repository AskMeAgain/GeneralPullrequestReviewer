package io.github.askmeagain.pullrequest.dto.gitlab.project;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;


@Data
public class ContainerExpirationPolicy{
    public String cadence;
    public boolean enabled;
    public int keep_n;
    public String older_than;
    public String name_regex;
    public Object name_regex_keep;
    public Date next_run_at;
}

