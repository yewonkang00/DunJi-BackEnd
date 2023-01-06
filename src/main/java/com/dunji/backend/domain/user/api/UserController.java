package com.dunji.backend.domain.user.api;

import com.dunji.backend.domain.user.application.RegisterService;
import com.dunji.backend.domain.user.application.UserService;
import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.domain.user.dto.UserDto;
import com.dunji.backend.global.common.CommonCode;
import com.dunji.backend.global.common.CommonResponse;
import com.dunji.backend.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final RegisterService registerService;

    private final String ACCESS_TOKEN = "access_token";
//    private final String REFRESH_TOKEN =  //TODO : 카카오 refresh token은 저장해둘 필요 없을까? 탈퇴나 로그아웃 시
//    private final String X_ACCESS_TOKEN = "x-access-token";
//    private final String X_REFRESH_TOKEN = "x-refresh-token";
    private final int COOKIE_MAX_AGE = 30*60*60*24; // 30일

    //카카오 로그인
    @GetMapping("/kakao")
    public CommonResponse kakaoCallback(@RequestParam String code, HttpServletResponse response) throws Exception {
        HashMap<String, String> token = userService.getKakaoAccessToken(code);
        String access_token = token.get(ACCESS_TOKEN);
//        String refresh_token = token.get(REFRESH_TOKEN);

        User userInfo = userService.getKakaoUserInfo(access_token);
        //TODO : toDto() 도메인 메서드화
        UserDto userDto = UserDto.builder()
                .email(userInfo.getEmail())
                .nickname(userInfo.getNickname())
                .ci(userInfo.getCi())
                .build();

        User newUser = registerService.userSave(userDto);

        //jwt 토큰 발급
        String jwtAccessToken = jwtTokenProvider.createToken(newUser.getUserId().toString(), userInfo.getRoles(), jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        String jwtRefreshToken = jwtTokenProvider.createToken(newUser.getUserId().toString(), userInfo.getRoles(), jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);

        // TODO : 쿠키 로직 분리하기
        Cookie accessCookie = new Cookie(jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, jwtAccessToken);
        accessCookie.setMaxAge(COOKIE_MAX_AGE);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
//        cookie.setSecure(true); //https 상에서만 동작

        Cookie refreshCookie = new Cookie(jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME, jwtRefreshToken);
        refreshCookie.setMaxAge(COOKIE_MAX_AGE);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
//        cookie.setSecure(true); //https 상에서만 동작

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

//        ResponseCookie cookie = ResponseCookie.from("access_token", access_token)
//                .httpOnly(true) //쿠키 httpOnly 설정
//                .secure(true) // 쿠키 Secure 설정. https에서만 true 사용 가능
//                .maxAge(MAX_AGE) // 쿠키 유효시간
//                .sameSite("None") // 서로다른 도메인 간 쿠키전송 제한. None이면 Secure설정이 true여야만 쿠키 전송 가능
//                .path("/") //쿠키를 열어볼 수 있는 위치 설정. 해당url의 하위 url에서만 이 쿠키를 전달함. 기본값은 쿠키를 생성한 장소
//                .build();

        return CommonResponse.toResponse(CommonCode.OK, "uuid : "+newUser.getUserId());
    }
}
