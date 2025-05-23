package me.bzo.bzo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 고유식별자

    private Long userId;
    private String title; // 제목
    private String content; // 내용
    private String writer; // 작성자
    private String board; // 게시판
    private LocalDateTime createdAt; // 작성시간
    private String imageUrl; // 이미지 URL

    @ManyToMany
    @JoinTable(
            name = "post_hashtag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private List<Hashtag> hashtags = new ArrayList<Hashtag>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    //수정 메서드 추가
    public void update(String title, String content, List<Hashtag> hashtags){
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
    }
}