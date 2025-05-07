package me.bzo.bzo.controller;

import lombok.RequiredArgsConstructor;
import me.bzo.bzo.entity.Post;
import me.bzo.bzo.repository.PostRepository;
import me.bzo.bzo.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
@RequiredArgsConstructor
@Controller
public class ViewController {
    private final PostRepository postRepository;

    //로그인 화면
    @GetMapping("/login")
    private String login(Model model) {
        return "login";
    }

    //회원가입 화면
    @GetMapping("/signup")
    private String signup(Model model) {
        return "signup";
    }
    //플랫폼 메인화면
    @GetMapping("/")
    private String home(Model model) {
        try {
            List<Post> posts = postRepository.findAll();
            model.addAttribute("posts", posts);
            return "main";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 조회 중 오류가 발생했습니다.");
            return "error";
        }
    }

    //게시글 수정 폼으로 이동
    @GetMapping("posts/{id}/edit")
    public String editPost(@PathVariable Long id, Model model) {
        Post updatePost = postRepository.findById(id).orElse(null);
        model.addAttribute("post", updatePost);
        return "editPost";
    }

    //게시글 작성 폼으로 이동
    @GetMapping("/posts/new")
    public String createPostForm(Model model, Principal principal) {
        if(principal == null) {
            return "redirect:/login";
        }

        model.addAttribute("name", principal.getName());
        return "createPostForm";
    }

    //게시글 상세보기
    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        model.addAttribute("post", post);
        return "postDetail";
    }

}