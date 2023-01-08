package com.dunji.backend.global.config.security.jwt;

import com.dunji.backend.domain.user.application.AuthService;
import com.dunji.backend.global.common.error.AuthException;
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
    private final AuthService authService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, AuthException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        
        boolean isAccessTokenValid = jwtTokenProvider.isTokenValidByServlet(httpServletRequest, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        
        //access token 유효
        if(isAccessTokenValid) {
            try{
                Authentication authentication = jwtTokenProvider.getAuthenticationByServlet(httpServletRequest, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("jwtAuthenticationFilter : access token is valid");
            }catch (AuthException authException){
                //token은 유효기간이 남았는데 탈퇴 등을 해서 DB에는 이 userPK에 해당하는 user가 없음 -> 모든 토큰이 유효하지 않은 상황과 동일 취급
                log.info("jwtAuthenticationFilter : access token is same as invalid. No user exist with this userPK");
            }
        }
        //access token 유효하지 않음
        else {
            log.info("jwtAuthenticationFilter : access token is invalid");

            //refresh token 유효, access token 재발급
            //RTR 적용 : refresh token 도 새로 발급
            if(jwtTokenProvider.isTokenValidByServlet(httpServletRequest, jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME)) {
                log.info("jwtAuthenticationFilter : refresh token is valid. Recreate access token");
                String uuid = jwtTokenProvider.getUserPKByServlet(httpServletRequest, jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);
                String newAccessToken = "";
                String newRefreshToken = "";

                try {
                    List<String> roles = authService.getUserByUuid(uuid).getRoles();
                    newAccessToken = jwtTokenProvider.createToken(uuid, roles, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
                    newRefreshToken = jwtTokenProvider.createToken(uuid, roles, jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);

                }catch (Exception e) {
                    log.info("jwtAuthenticationFilter Exception : "+e.getMessage());
                    //e.getStackTrace();
                }

                log.info("jwtAuthenticationFilter : Set new access token & refresh token in cookie");
                //TODO : 중복 로직 정리
                Cookie accessCookie = new Cookie(jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, newAccessToken);
                accessCookie.setMaxAge(jwtTokenProvider.ACCESS_COOKIE_MAX_AGE);
                accessCookie.setHttpOnly(true);
                accessCookie.setPath("/");
//        cookie.setSecure(true); //https 상에서만 동작

                Cookie refreshCookie = new Cookie(jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME, newRefreshToken);
                refreshCookie.setMaxAge(jwtTokenProvider.REFRESH_COOKIE_MAX_AGE);
                refreshCookie.setHttpOnly(true);
                refreshCookie.setPath("/");
//        cookie.setSecure(true); //https 상에서만 동작

                ((HttpServletResponse)response).addCookie(accessCookie);
                ((HttpServletResponse)response).addCookie(refreshCookie);

            }
            //refresh token 유효하지 않음
            else {
                log.info("jwtAuthenticationFilter : refresh token is invalid. Guest user");
            }
        }
        chain.doFilter(request, response);
    }
}
