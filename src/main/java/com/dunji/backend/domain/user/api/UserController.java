package com.dunji.backend.domain.user.api;

import com.dunji.backend.domain.user.application.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //카카오 로그인
    @ResponseBody
    @GetMapping("/kakaoLogin")
    public void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws Exception {
        String access_Token = userService.getKakaoAccessToken(code);
        userService.getUserInfo(access_Token);

        Cookie cookie = new Cookie("access_token", access_Token);
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);
    }
}
