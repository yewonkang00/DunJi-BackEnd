package com.dungzi.backend.global.config.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.socket.WebSocketHttpHeaders;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



@Slf4j
public class JwtTokenWebSocketFilter extends OncePerRequestFilter {
    private static final String COOKIE_ACCESS_TOKEN = "x-access-token";
    private static final String COOKIE_REFRESH_TOKEN = "x-refresh-token";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> cookieJwtToken = extractJwtTokenFromCookie(request);
        if (cookieJwtToken.size() != 0) {
            log.debug("websocket header에 토큰주입");
            WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
            headers.add(COOKIE_ACCESS_TOKEN, cookieJwtToken.get(COOKIE_ACCESS_TOKEN));
            headers.add(COOKIE_REFRESH_TOKEN, cookieJwtToken.get(COOKIE_REFRESH_TOKEN));

        }
        filterChain.doFilter(request, response);
    }

    private Map<String, String> extractJwtTokenFromCookie(HttpServletRequest request) {
        Map<String, String> cookieJwtToken = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_ACCESS_TOKEN.equals(cookie.getName())) {
                    cookieJwtToken.put(COOKIE_ACCESS_TOKEN,cookie.getValue());
                } else if (COOKIE_REFRESH_TOKEN.equals(cookie.getName())) {
                    cookieJwtToken.put(COOKIE_REFRESH_TOKEN,cookie.getValue());
                }
            }
        }
        return cookieJwtToken;
    }
}
