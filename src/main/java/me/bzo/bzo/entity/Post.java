package me.bzo.bzo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 고유식별자

    private String title; // 제목
    private String content; // 내용
    private String writer; // 작성자
    private String board; // 게시판
    private LocalDateTime createdAt; // 작성시간
    private String imageUrl; // 이미지 URL

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
