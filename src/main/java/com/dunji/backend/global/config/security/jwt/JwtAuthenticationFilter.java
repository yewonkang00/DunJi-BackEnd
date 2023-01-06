package com.dunji.backend.global.config.security.jwt;

import com.dunji.backend.domain.user.application.UserService;
import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.global.common.CommonCode;
import com.dunji.backend.global.common.error.AuthException;
import com.dunji.backend.global.common.error.CommonErrorCode;
import com.dunji.backend.global.config.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean { //GenericFilterBean 필터 자동 등록

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
//    private final CustomUserDetailsService customUserDetailsService;
    private final int COOKIE_MAX_AGE = 60*60*24; // 1일

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, AuthException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean isAccessTokenValid = false;
        try{
            isAccessTokenValid = jwtTokenProvider.isTokenValidByServlet(httpServletRequest, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        }catch (AuthException authException) {
            // 비회원일 경우 토큰 확인 안함
            if(authException.getCode() == CommonErrorCode.GUEST_USER){
                return;
            }
        }

        //access token 유효
        if(isAccessTokenValid) {
            Authentication authentication = jwtTokenProvider.getAuthenticationByServlet(httpServletRequest, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //access token 유효하지 않음
        else {
            log.info("jwtAuthenticationFilter : access token is invalid");

            //refresh token 유효, access token 재발급
            if(jwtTokenProvider.isTokenValidByServlet(httpServletRequest, jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME)) {
                log.info("jwtAuthenticationFilter : refresh token is valid. Recreate access token");
                String uuid = jwtTokenProvider.getUserPKByServlet(httpServletRequest, jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);
                String newAccessToken = "";

                try {
                    List<String> roles = userService.getUserByUuid(uuid).getRoles();
                    newAccessToken = jwtTokenProvider.createToken(uuid, roles, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
                }catch (Exception e) {
                    log.info("jwtAuthenticationFilter Exception : "+e.getMessage());
                    //e.getStackTrace();
                }

                //TODO : 될까...
                Cookie cookie = new Cookie(jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, newAccessToken);
                cookie.setMaxAge(COOKIE_MAX_AGE);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
//        cookie.setSecure(true); //https 상에서만 동작
                ((HttpServletResponse)response).addCookie(cookie);

            }
            //refresh token 유효하지 않음
            else {
                log.info("jwtAuthenticationFilter : refresh token is invalid. Need Sign up");
//                throw new AuthException(CommonCode.UNAUTHORIZED); //TODO : Handling

            }
        }
        chain.doFilter(request, response);
    }
}
