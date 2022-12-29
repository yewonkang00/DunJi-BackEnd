package com.dunji.backend.domain.user.api;

import com.dunji.backend.domain.user.application.UserService;
import com.dunji.backend.global.common.CommonCode;
import com.dunji.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class UserController {
    private final UserService userService;

    //카카오 로그인
    @GetMapping("/kakao")
    public CommonResponse kakaoCallback(@RequestParam String code, HttpServletResponse response) throws Exception {
        String access_Token = userService.getKakaoAccessToken(code);
        userService.getUserInfo(access_Token);

        Cookie cookie = new Cookie("access_token", access_Token);
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);

        return CommonResponse.toResponse(CommonCode.OK, response);
    }
}
