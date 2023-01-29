package com.dungzi.backend.global.config.security.interceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
@Component
public class JwtTokenCookieInterceptor implements HandshakeInterceptor {

    private static final String COOKIE_ACCESS_TOKEN = "x-access-token";
    private static final String COOKIE_REFRESH_TOKEN = "x-refresh-token";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes){
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        Map<String, String> cookieJwtToken = extractJwtTokenFromCookie(servletRequest);
        attributes.put(COOKIE_ACCESS_TOKEN, cookieJwtToken.get(COOKIE_ACCESS_TOKEN));
        attributes.put(COOKIE_REFRESH_TOKEN, cookieJwtToken.get(COOKIE_REFRESH_TOKEN));
        log.info("websocket header에 토큰주입");
        log.info("{}:{}", COOKIE_ACCESS_TOKEN, cookieJwtToken.get(COOKIE_ACCESS_TOKEN));
        log.info("{}:{}", COOKIE_REFRESH_TOKEN, cookieJwtToken.get(COOKIE_REFRESH_TOKEN));
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    private Map<String, String> extractJwtTokenFromCookie(HttpServletRequest request) {
        Map<String, String> cookieJwtToken = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_ACCESS_TOKEN.equals(cookie.getName())) {
                    cookieJwtToken.put(COOKIE_ACCESS_TOKEN, cookie.getValue());
                } else if (COOKIE_REFRESH_TOKEN.equals(cookie.getName())) {
                    cookieJwtToken.put(COOKIE_REFRESH_TOKEN, cookie.getValue());
                }
            }
        }
        return cookieJwtToken;
    }
}
