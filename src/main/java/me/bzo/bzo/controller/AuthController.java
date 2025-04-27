package me.bzo.bzo.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.bzo.bzo.config.SecurityConfig;
import me.bzo.bzo.dto.LoginRequest;
import me.bzo.bzo.dto.RegisterReuqest;
import me.bzo.bzo.dto.TokenResponse;
import me.bzo.bzo.entity.Users;
import me.bzo.bzo.service.AuthService;
import me.bzo.bzo.util.CookieUtil;
import me.bzo.bzo.util.JwtProvider;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final SecurityConfig securityConfig;

    // 회원가입
    @PostMapping("/register")
    private ResponseEntity<String> register(@RequestBody RegisterReuqest request){
        authService.register(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인
    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response){
        // 로그인처리
        Users user = authService.login(request);
        // 토큰 생성
        String token = jwtProvider.createToken(user.getEmail());
        // 쿠키 생성 및 응답에 추가
        response.addCookie(CookieUtil.createTokenCookie(token));
        return ResponseEntity.ok(token);
    }
}
