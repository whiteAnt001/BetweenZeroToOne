package me.bzo.bzo.controller;

import lombok.RequiredArgsConstructor;
import me.bzo.bzo.dto.PostRequest;
import me.bzo.bzo.entity.Post;
import me.bzo.bzo.service.PostService;
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

    //게시글 작성
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

    //게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Long id, @RequestBody PostRequest postRequest) {
        Post updatePost = postService.updatePost(id, postRequest);
        return ResponseEntity.ok(updatePost);
    }
}