package me.bzo.bzo.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import me.bzo.bzo.entity.Users;
import me.bzo.bzo.repository.UserRepository;
import me.bzo.bzo.util.JwtUtil;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    // JWT 토큰에서 사용자 정보 추출
    public Users getUserInfoFormToken(String token) {
        Claims claims = jwtUtil.getClaims(token);
        String email = claims.getSubject(); // 이메일(sub)

        //이메일로 유저 객체 가져오기
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
