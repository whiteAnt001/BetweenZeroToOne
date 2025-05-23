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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;

    //게시글 목록 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(Long id, Long requestUserId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if(!post.getUserId().equals(requestUserId)){
            throw new SecurityException("삭제 권한이 없습니다.");
        }

        // 해당 게시글과 연결된 해시태그 연결끊기(제거)
        post.getHashtags().clear();
        postRepository.save(post); // 연결끊고 세이브 해줘야함 안 그럼 삭제 안 될 가능성 있음

        //이후 삭제
        postRepository.delete(post);
    }
    //게시글 수정
    @Transactional
    public Post updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        //해시태그 변환하여 저장
        List<Hashtag> hashtags = postRequest.getHashtags().stream()
                .map(name -> hashtagRepository.findByName(name).orElseGet(() -> { Hashtag newHashTag = new Hashtag(name);
                    return hashtagRepository.save(newHashTag);
                }))
                .collect(Collectors.toList());

        post.update(postRequest.getTitle(), postRequest.getContent(), hashtags);
        return postRepository.save(post);
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
            hashtags.add(hashTag);
        }
        post.setHashtags(hashtags);

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