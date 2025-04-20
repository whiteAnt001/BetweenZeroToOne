package me.bzo.bzo.controller;

import me.bzo.bzo.entity.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {
    //게시글 작성 폼으로 이동
    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        return "createPostForm";
    }
}
