package me.bzo.bzo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.bzo.bzo.dto.PostRequest;
import me.bzo.bzo.entity.Hashtag;
import me.bzo.bzo.entity.Post;
import me.bzo.bzo.repository.HashtagRepository;
import me.bzo.bzo.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    //게시글 목록 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    //게시글 생성
    @Transactional
    public Post createPost(PostRequest postRequest, MultipartFile image) throws IOException {
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
        post.setTitle(postRequest.getTitle());
        post.setWriter(postRequest.getWriter());
        post.setContent(postRequest.getContent());
        post.setBoard(postRequest.getBoard());
        post.setImageUrl(imageUrl);

        //해시태그 처리
        List<Hashtag> hashtags = new ArrayList<>();
        for(String tagName : postRequest.getHashtags()){
            Hashtag hashTag = hashtagRepository.findByName(tagName).orElseGet(() ->hashtagRepository.save(new Hashtag(tagName)));
        }

        return postRepository.save(post); //게시글 저장
    }

    // 해시태그 연결
    private List<Hashtag> extractAndSaveHashtags(String content) {
        List<Hashtag> hashtags = new ArrayList<>();
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            //중복 방지를 위해 소문자로 처리
            String tagName = matcher.group(1).toLowerCase();

            //이미 존재하는 해시태그인지 확인
            Hashtag hashtag = hashtagRepository.findByName(tagName).orElseGet(() -> {
                Hashtag newTag = new Hashtag();
                newTag.setName(tagName);
                return hashtagRepository.save(newTag);
            });hashtags.add(hashtag);
        }
        return hashtags;
    }
}
