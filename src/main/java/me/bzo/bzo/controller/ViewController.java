package me.bzo.bzo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.bzo.bzo.dto.PostRequest;
import me.bzo.bzo.entity.Post;
import me.bzo.bzo.entity.Users;
import me.bzo.bzo.repository.PostRepository;
import me.bzo.bzo.service.UserService;
import me.bzo.bzo.util.JwtUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@RequiredArgsConstructor
@Controller
public class ViewController {
    private final JwtUtil jwtUtil;
    private final PostRepository postRepository;
    private final UserService userService;

    // 로그인 화면
    @GetMapping("/login")
    private String login(Model model) {
        return "login";
    }

    // 회원가입 화면
    @GetMapping("/signup")
    private String signup(Model model) {
        return "signup";
    }

    // 플랫폼 메인화면
    @GetMapping("/")
    private String home(HttpServletRequest request, Model model) {
        try {
            // 게시글 목록 조회
            List<Post> posts = postRepository.findAll();
            model.addAttribute("posts", posts);

            // 쿠키에서 accessToken 가져오기 (이전에 저장한 이름과 일치시켜야 함)
            String token = jwtUtil.getTokenFromCookieByName(request, "accessToken");

            if (token != null && jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 추출
                Users user = userService.getUserInfoFormToken(token);
                if (user != null) {
                    model.addAttribute("userName", user.getName());
                    model.addAttribute("userEmail", user.getEmail());
                    model.addAttribute("userRole", user.getRole());
                }
            } else {
                // 토큰이 없거나 유효하지 않으면 로그인 여부 표시용으로만 사용
                model.addAttribute("userName", null);
            }

            return "main";
        } catch (Exception e) {
            e.printStackTrace();  // 디버깅용
            model.addAttribute("errorMessage", "게시글 조회 중 오류가 발생했습니다.");
            return "error";
        }
    }

    // QnA게시글 리스트로 이동
    @GetMapping("/posts/qna")
    public String qnaList(Model model) {
        List<Post> posts = postRepository.findByBoard("qna");
        model.addAttribute("posts", posts);
        return "post/board";
    }

    // 커뮤니티 게시글 리스트로 이동
    @GetMapping("/posts/community")
    public String communityList(Model model) {
        List<Post> posts = postRepository.findByBoard("community");
        model.addAttribute("posts", posts);
        return "post/board";
    }

    // Tip 게시글 리스트로 이동
    @GetMapping("/posts/tip")
    public String tipList(Model model) {
        List<Post> posts = postRepository.findByBoard("tip");
        model.addAttribute("posts", posts);
        return "post/board";
    }

    // etc 게시글 리스트로 이동
    @GetMapping("/posts/etc")
    public String etcList(Model model) {
        List<Post> posts = postRepository.findByBoard("etc");
        model.addAttribute("posts", posts);
        return "post/board";
    }

    // 게시글 수정 폼으로 이동
    @GetMapping("/posts/{id}/edit")
    public String editPost(@PathVariable Long id, Model model) {
        Post updatePost = postRepository.findById(id).orElse(null);
        model.addAttribute("post", updatePost);
        return "post/editPost";
    }

    // 게시글 작성 폼으로 이동
    @GetMapping("/posts/new")
    public String createPostForm(Model model, HttpServletRequest request) {
        String token = jwtUtil.getTokenFromCookieByName(request, "accessToken");

        if(token == null || !jwtUtil.validateToken(token)) {
            return "redirect:/login";
        }

        String userName = jwtUtil.getUserNameFromToken(token);  // 토큰에서 사용자 이름(실명 또는 닉네임) 꺼내기
        model.addAttribute("userName", userName);
        model.addAttribute("postRequest", new PostRequest());

        return "createPostForm";
    }

    // 게시글 상세보기
    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).orElse(null);
        model.addAttribute("post", post);
        return "post/postDetail";
    }

}