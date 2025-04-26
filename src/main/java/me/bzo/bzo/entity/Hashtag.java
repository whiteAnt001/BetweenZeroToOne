package me.bzo.bzo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;

    @ManyToMany(mappedBy = "hashtags")
    private List<Post> posts = new ArrayList<Post>();

    public Hashtag(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
