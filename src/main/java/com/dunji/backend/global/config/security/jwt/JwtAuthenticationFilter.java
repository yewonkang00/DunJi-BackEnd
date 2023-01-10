package com.dunji.backend.global.config.security.jwt;

import com.dunji.backend.domain.user.application.AuthService;
import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.global.common.error.AuthException;
import com.dunji.backend.global.common.error.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean { //GenericFilterBean 필터 자동 등록

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, AuthException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        //TODO : servlet 에서 getToken을 두번식 하는 게 됐는데 토큰을 getToken을 public 으로 바꿔서 여기서 꺼낼까..그럼 예외처리는..
        boolean isAccessTokenValid = jwtTokenProvider.isTokenValidByServlet(httpServletRequest, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        
        //access token 유효
        if(isAccessTokenValid) {
            try{
                Authentication authentication = jwtTokenProvider.getAuthenticationByServlet(httpServletRequest, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
                log.info("jwtAuthenticationFilter : access token is valid");

                SecurityContextHolder.getContext().setAuthentication(authentication);
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
                log.info("jwtAuthenticationFilter : refresh token is valid. Recreate tokens");
                String uuid = jwtTokenProvider.getUserPKByServlet(httpServletRequest, jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);

                try {
                    User user = authService.getUserByUuid(uuid);

                    log.info("jwtAuthenticationFilter : Set new access token & refresh token in cookie");
                    authService.setTokenCookieAndSecurityByUser((HttpServletResponse)response, user);

                }catch (AuthException authException) {
                    if(authException.getCode() == CommonErrorCode.NOT_EXIST_USER){
                        log.info("jwtAuthenticationFilter AuthException : "+authException.getMessage());
                    }
                }
                catch (Exception e) {
                    log.info("jwtAuthenticationFilter Exception : {}", e.getStackTrace());
                    throw e;
                }

            }
            //refresh token 유효하지 않음
            else {
                log.info("jwtAuthenticationFilter : refresh token is invalid. Guest user");
            }
        }
        chain.doFilter(request, response);
    }
}
