package me.bzo.bzo.dto;

import lombok.Data;

@Data
public class GitHubRepoRequest {
    private String name;
    private String html_url;
    private String description;
}
