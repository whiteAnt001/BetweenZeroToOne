package me.bzo.bzo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.bzo.bzo.dto.PostRequest;
import me.bzo.bzo.entity.Post;
import me.bzo.bzo.service.PostService;
import me.bzo.bzo.util.JwtUtil;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    // 게시글 작성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestPart("post") PostRequest postRequest,
                                        @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Post post = postService.createPost(postRequest, image);
            return ResponseEntity.ok(post);
        }catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
        }
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody PostRequest postRequest) {
        Post updatePost = postService.updatePost(id, postRequest);
        return ResponseEntity.ok(updatePost);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id, HttpServletRequest request) {
        // 쿠키에서 토큰 추출
        String token = jwtUtil.getTokenFromCookieByName(request, "accessToken");

        if(token == null || !jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        
        // 토큰에서 userId 추출
        Long userId = jwtUtil.getUserIdFromToken(token);

        try{
            //삭제 서비스 호출시 작성자 검증
            postService.deletePost(id, userId);
            return ResponseEntity.ok("게시글이 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }

    }
}