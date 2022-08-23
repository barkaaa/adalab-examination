package com.adalab.examination.entity;

import lombok.Data;

@Data
public class GitHubUser {
    private String login;
    private int id;
    private String avatar_url;
    private String email;
}
