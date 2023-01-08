package com.dunji.backend.domain.user.api;

import com.dunji.backend.domain.user.application.AuthService;
import com.dunji.backend.domain.user.application.KakaoService;
import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.global.common.CommonCode;
import com.dunji.backend.global.common.CommonResponse;
import com.dunji.backend.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class UserController {
    private final KakaoService kakaoService;
    private final AuthService authService;

    private final String ACCESS_TOKEN = "access_token";
//    private final String REFRESH_TOKEN = "refresh_token"; //TODO : 카카오 refresh token은 저장해둘 필요 없을까? 탈퇴나 로그아웃 시

    //카카오 로그인
    @GetMapping("/kakao")
    public CommonResponse kakaoCallback(@RequestParam String code, HttpServletResponse httpServletResponse) throws Exception {
        log.info("[API] login/kakao");

        HashMap<String, String> token = kakaoService.getKakaoAccessToken(code);
        String kakao_access_token = token.get(ACCESS_TOKEN);
//        String refresh_token = token.get(REFRESH_TOKEN);

        User kakaoUser = kakaoService.getKakaoUserInfo(kakao_access_token);

        User loginUser = authService.userLoginWithSignUp(kakaoUser);

        authService.setCookieTokenByUser(httpServletResponse, loginUser);


        // TODO : 토큰 로직 분리하기
        //jwt 토큰 발급
/*        String jwtAccessToken = jwtTokenProvider.createToken(loginUser.getUserId().toString(), kakaoUser.getRoles(), jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        String jwtRefreshToken = jwtTokenProvider.createToken(loginUser.getUserId().toString(), kakaoUser.getRoles(), jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);
//        log.info("[API] login/kakao accessToken : "+jwtAccessToken);
//        log.info("[API] login/kakao refreshToken : "+jwtRefreshToken);


        // TODO : 쿠키 로직 분리하기 - Filter와 중복로직
        Cookie accessCookie = new Cookie(jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, jwtAccessToken);
        accessCookie.setMaxAge(jwtTokenProvider.ACCESS_COOKIE_MAX_AGE);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
//        cookie.setSecure(true); //https 상에서만 동작

        Cookie refreshCookie = new Cookie(jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME, jwtRefreshToken);
        refreshCookie.setMaxAge(jwtTokenProvider.REFRESH_COOKIE_MAX_AGE);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
//        cookie.setSecure(true); //https 상에서만 동작

//        log.info("[API] login/kakao accessCookie getMaxAge : "+accessCookie.getMaxAge());
//        log.info("[API] login/kakao refreshCookie getMaxAge : "+refreshCookie.getMaxAge());
        httpServletResponse.addCookie(accessCookie);
        httpServletResponse.addCookie(refreshCookie);*/

        return CommonResponse.toResponse(CommonCode.OK, "uuid : "+loginUser.getUserId());
    }
}
