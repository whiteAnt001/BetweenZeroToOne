package me.bzo.bzo.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.bzo.bzo.config.SecurityConfig;
import me.bzo.bzo.dto.LoginRequest;
import me.bzo.bzo.dto.RegisterReuqest;
import me.bzo.bzo.service.AuthService;
import me.bzo.bzo.util.JwtProvider;
import me.bzo.bzo.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final SecurityConfig securityConfig;
    private final JwtUtil jwtUtil;

    // 회원가입
    @PostMapping("/register")
    private ResponseEntity<String> register(@RequestBody RegisterReuqest request){
        authService.register(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    // 로그인
    @PostMapping("/login")
    private ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletResponse response){
        try {
            authService.login(request,response);
            return ResponseEntity.ok("로그인 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
