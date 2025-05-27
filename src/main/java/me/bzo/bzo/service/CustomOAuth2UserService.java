package me.bzo.bzo.service;

import lombok.RequiredArgsConstructor;
import me.bzo.bzo.entity.Users;
import me.bzo.bzo.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2user = super.loadUser(userRequest);

        String accessToken = userRequest.getAccessToken().getTokenValue();
        String email = oAuth2user.getAttribute("email");
        String name = oAuth2user.getAttribute("name");


        // DB에 사용자가 없으면 저장, 있으면 업데이트
       Optional<Users> optionalUser = userRepository.findByEmail(email);

       Users user;
       // 유저가 있을 경우
       if(optionalUser.isPresent()) {
           user = optionalUser.get();
           // 토큰만 업뎃
           user.setGithubToken(accessToken);
       } else {
           // 유저가 없을 경우
           user = new Users();
           user.setEmail(email);
           user.setName(name);
           user.setGithubToken(accessToken);
           user.setProvider("github");
           user.setRole("ROLE_USER");
       }
        userRepository.save(user);
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2user.getAttributes(),
                "email"
        );
    }

    private String getTokenFromUser(OAuth2User user) {
        String email = user.getAttribute("email");
        Users dbUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자 정보가 없습니다."));
        return dbUser.getGithubToken();
    }

}
