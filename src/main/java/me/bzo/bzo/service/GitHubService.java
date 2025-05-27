package me.bzo.bzo.service;

import me.bzo.bzo.dto.GitHubRepoRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Service
public class GitHubService {

    public List<GitHubRepoRequest> fetchUserRepos(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<GitHubRepoRequest[]> response = restTemplate.exchange(
                "https://api.github.com/user/repos",
                HttpMethod.GET,
                entity,
                GitHubRepoRequest[].class
        );
        return Arrays.asList(response.getBody());
    }
}
