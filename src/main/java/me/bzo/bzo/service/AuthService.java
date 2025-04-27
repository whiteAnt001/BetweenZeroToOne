package me.bzo.bzo.service;

import lombok.RequiredArgsConstructor;
import me.bzo.bzo.util.JwtProvider;
import me.bzo.bzo.util.JwtUtil;
import me.bzo.bzo.config.SecurityConfig;
import me.bzo.bzo.dto.LoginRequest;
import me.bzo.bzo.dto.RegisterReuqest;
import me.bzo.bzo.entity.Users;
import me.bzo.bzo.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final SecurityConfig securityConfig;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    //회원가입 로직
    public void register(RegisterReuqest request) {
        String encodedPassword = securityConfig.passwordEncoder().encode(request.getPassword());
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        Users users = Users.builder()
                .email(request.getEmail())
                .password(encodedPassword)
                .name(request.getName())
                .role("USER")
                .build();

        userRepository.save(users);
    }

    //로그인 로직
    public Users login(LoginRequest request) {
        Users users = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일이 존재하지 않습니다."));

        if(!securityConfig.passwordEncoder().matches(request.getPassword(), users.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }

        //로그인 성공시 토큰 발급
        return users;
    }
    
    //토큰 생성 메서드
    public String createToken(Users user) {
        return jwtProvider.createToken(user.getEmail());
    }
    // 비밀번호 해싱
    public String encryptPassword(String rawPassword) {
        return securityConfig.passwordEncoder().encode(rawPassword);
    }
}
