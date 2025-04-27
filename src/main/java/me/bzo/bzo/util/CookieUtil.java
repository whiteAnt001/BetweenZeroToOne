package me.bzo.bzo.util;

import jakarta.servlet.http.Cookie;

public class CookieUtil {
    //쿠키 생성
    public static Cookie createTokenCookie(String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); //악성 스크립트 방지
        cookie.setSecure(true); //Http 환경에선 false, https환경에선 true (이거땜시 저번에 엄청 고생함ㅁ)
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 1일
        return cookie;
    }
    // 쿠키 삭제
    public static Cookie deleteTokenCookie(){
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); //즉시만료
        return cookie;
    }
}
