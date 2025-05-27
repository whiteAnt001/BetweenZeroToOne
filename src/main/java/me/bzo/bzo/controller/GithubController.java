package me.bzo.bzo.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import me.bzo.bzo.dto.GitHubRepoRequest;
import me.bzo.bzo.entity.Users;
import me.bzo.bzo.repository.UserRepository;
import me.bzo.bzo.service.GitHubService;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GithubController {

    private final GitHubService gitHubService;
    private final UserRepository userRepository;

    @GetMapping("/repos")
    public ResponseEntity<?> getUserRepos(@AuthenticationPrincipal OAuth2User user) {
        String accessToken = getTokenFromUser(user);
        List<GitHubRepoRequest> repos = gitHubService.fetchUserRepos(accessToken);
        return ResponseEntity.ok(repos);
    }

    private String getTokenFromUser(OAuth2User oAUth2User) {
        String email = oAUth2User.getAttribute("email");
        System.out.println("로그인 사용자 이메일" + email);
        if (email == null) {
            throw new RuntimeException("OAuth2 사용자 정보에서 이메일을 찾을 수 없습니다.");
        }

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 사용자 정보가 없습니다."));

        String token = user.getGithubToken();
        if(token == null) {
            throw new RuntimeException("GitHub 토큰이 저장되어 있지 않습니다.");
        }
        return token;
    }
}
