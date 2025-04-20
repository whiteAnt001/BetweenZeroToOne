package me.bzo.bzo.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    private String title;
    private String content;
    private String writer;
    private String board;
    private List<String> hashtags;
}
