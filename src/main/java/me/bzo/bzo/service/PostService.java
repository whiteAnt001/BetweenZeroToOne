package me.bzo.bzo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.bzo.bzo.entity.Post;
import me.bzo.bzo.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    //게시글 목록 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    //게시글 생성
    @Transactional
    public Post createPost(String title, String content, String writer, String board, MultipartFile image) throws IOException {
        String imageUrl = null;

        //이미지 파일이 있는 경우
        if(image != null && !image.isEmpty()){
            String uploadDirectory = "src/main/resources/static/uploads/";
            Path path = Paths.get(uploadDirectory + image.getOriginalFilename());
            Files.copy(image.getInputStream(), path);
            imageUrl = "/uploads/" + image.getOriginalFilename(); // 저장된 이미지 URL
        }
        //게시글 생성
        Post post = new Post();
        post.setTitle(title);
        post.setWriter(writer);
        post.setContent(content);
        post.setImageUrl(imageUrl);

        return postRepository.save(post); //게시글 저장
    }
}
