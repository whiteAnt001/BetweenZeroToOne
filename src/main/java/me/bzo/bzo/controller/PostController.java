package me.bzo.bzo.controller;

import lombok.RequiredArgsConstructor;
import me.bzo.bzo.entity.Post;
import me.bzo.bzo.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;

    //게시글 조회
    @GetMapping("/questions")
    public String getPosts(Model model){
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "questionsList";
    }
    //게시글 작성 폼으로 이동
    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        return "createPostForm";
    }

    //게시글 작성
    @PostMapping("/posts")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("writer") String writer,
                             @RequestParam("board") String board,
                             @RequestParam(value = "image", required = false) MultipartFile image,
                             Model model) throws IOException {
        postService.createPost(title, content, writer, board, image);
        return "redirect:/posts";
    }
}
