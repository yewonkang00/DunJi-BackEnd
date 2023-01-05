package com.dunji.backend.domain.user.api;

import com.dunji.backend.domain.user.application.RegisterService;
import com.dunji.backend.domain.user.application.UserService;
import com.dunji.backend.domain.user.dao.UserDao;
import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.domain.user.dto.UserDto;
import com.dunji.backend.global.common.CommonCode;
import com.dunji.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class UserController {
    private final UserService userService;
    private final RegisterService registerService;

    private final String ACCESS_TOKEN = "access_token";
//    private final String REFRESH_TOKEN = "refresh_token";
    private final String X_ACCESS_TOKEN = "x-access-token";
    private final String X_REFRESH_TOKEN = "x-refresh-token";
    private final int MAX_AGE = 60*60*24; // 1일

    //카카오 로그인
    @GetMapping("/kakao")
    public CommonResponse kakaoCallback(@RequestParam String code, HttpServletResponse response) throws Exception {
        HashMap<String, String> token = userService.getKakaoAccessToken(code);
        String access_token = token.get(ACCESS_TOKEN);
//        String refresh_token = token.get(REFRESH_TOKEN);

        User userInfo = userService.getUserInfo(access_token);
        //TODO : toDto() 도메인 메서드화
        UserDto user = UserDto.builder()
                .email(userInfo.getEmail())
                .nickname(userInfo.getNickname())
                .ci(userInfo.getCi())
                .build();

        String uuid = registerService.userSave(user);

        //jwt 토큰 발급


        // TODO : (현)kakao토큰 -> save한 user의 uuid 로 생성한 jwt 토큰으로 변경
        Cookie cookie = new Cookie(X_ACCESS_TOKEN, access_token);
        cookie.setMaxAge(MAX_AGE);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
//        cookie.setSecure(true); //https 상에서만 동작
        response.addCookie(cookie);

//        ResponseCookie cookie = ResponseCookie.from("access_token", access_token)
//                .httpOnly(true) //쿠키 httpOnly 설정
//                .secure(true) // 쿠키 Secure 설정. https에서만 true 사용 가능
//                .maxAge(MAX_AGE) // 쿠키 유효시간
//                .sameSite("None") // 서로다른 도메인 간 쿠키전송 제한. None이면 Secure설정이 true여야만 쿠키 전송 가능
//                .path("/") //쿠키를 열어볼 수 있는 위치 설정. 해당url의 하위 url에서만 이 쿠키를 전달함. 기본값은 쿠키를 생성한 장소
//                .build();

        return CommonResponse.toResponse(CommonCode.OK, "uuid : "+uuid);
    }
}
