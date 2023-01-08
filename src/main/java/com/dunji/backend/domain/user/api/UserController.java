package com.dunji.backend.domain.user.api;

import com.dunji.backend.domain.user.application.AuthService;
import com.dunji.backend.domain.user.application.KakaoService;
import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.global.common.CommonCode;
import com.dunji.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final KakaoService kakaoService;
    private final AuthService authService;

    private final String ACCESS_TOKEN = "access_token";
//    private final String REFRESH_TOKEN = "refresh_token"; //TODO : 카카오 refresh token은 저장해둘 필요 없을까? 탈퇴나 로그아웃 시

//    @PostMapping("/email-auth")
//    public CommonResponse updateUserEmailAuth(HttpServletRequest httpServletRequest) {
//
//    }

    //카카오 로그인
    @GetMapping("/login/kakao")
    public CommonResponse kakaoCallback(@RequestParam String code, HttpServletResponse httpServletResponse) throws Exception {
        log.info("[API] login/kakao");

        HashMap<String, String> token = kakaoService.getKakaoAccessToken(code);
        String kakao_access_token = token.get(ACCESS_TOKEN);
//        String refresh_token = token.get(REFRESH_TOKEN);

        User kakaoUser = kakaoService.getKakaoUserInfo(kakao_access_token);

        User loginUser = authService.userLoginWithSignUp(kakaoUser);

        authService.setTokenCookieAndSecurityByUser(httpServletResponse, loginUser);

        return CommonResponse.toResponse(CommonCode.OK, "uuid : "+loginUser.getUserId());
    }
}
