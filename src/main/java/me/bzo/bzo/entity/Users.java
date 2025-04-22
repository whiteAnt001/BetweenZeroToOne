package me.bzo.bzo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //고유식별자
    private String name; //이름
    private String email; //이메일
    private String password; //비밀번호
    private LocalDateTime createdAt; //계정 생성일


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
