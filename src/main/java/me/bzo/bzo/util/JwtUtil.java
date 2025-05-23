package me.bzo.bzo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import me.bzo.bzo.entity.Users;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long ACCESS_TOKEN_EXPIRATION = 900000L;  // 15분
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000L;  // 7일 (밀리초 단위)

    // 엑세스 토큰 생성
    public String generateAccessToken(Users user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("userName", user.getName())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 리프레시 토큰 생성
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT를 쿠키에 추가
    public void addJwtToCookie(HttpServletResponse response, String jwt, String cookieName) {
        Cookie cookie = new Cookie(cookieName, jwt);  // "jwt"라는 이름의 쿠키에 토큰 저장
        cookie.setHttpOnly(true);  // JavaScript에서 접근할 수 없도록 설정
        cookie.setSecure(true);    // HTTPS 프로토콜에서만 전송되도록 설정
        cookie.setPath("/");       // 모든 경로에서 접근 가능
        cookie.setMaxAge(86400);   // 1일 동안 쿠키 유효 (초 단위)
        response.addCookie(cookie);
    }
    //쿠키 이름으로 토큰 가져오기
    public String getTokenFromCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // JWT 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("토큰 검증 실패: " + e.getMessage());
        }
        return false;
    }

    // JWT에서 사용자 정보 추출
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    // JWT에서 사용자 이메일 추출
    public String getEmailFromToken(String token) {
        return getClaims(token).getSubject();
    }
    
    // JWT에서 사용자 이름 추출
    public String getUserNameFromToken(String token) {
        return getClaims(token).get("userName", String.class);
    }

    // JWT에서 사용자 고유 ID 추출
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId", Long.class);
    }

    // 쿠키 삭제용
    public void deleteJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        response.addCookie(cookie);
    }
}
