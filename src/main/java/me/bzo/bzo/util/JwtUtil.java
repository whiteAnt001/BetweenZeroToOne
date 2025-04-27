package me.bzo.bzo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    //랜덤 키 생성
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //토큰 생성
    public String generateToken(String email) {
        long now = System.currentTimeMillis();
        long exp = 1000 * 60 * 60; //유효시간 (1시간)

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + exp))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //토근 검증
    public String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
