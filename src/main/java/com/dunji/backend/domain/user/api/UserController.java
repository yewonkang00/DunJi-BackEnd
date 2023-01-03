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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class UserController {
    private final UserService userService;
    private final RegisterService registerService;
    //카카오 로그인
    @GetMapping("/kakao")
    public CommonResponse kakaoCallback(@RequestParam String code, HttpServletResponse response) throws Exception {
        HashMap<String, String> token = userService.getKakaoAccessToken(code);
        String access_token = token.get("access_token");
        String refresh_token = token.get("refresh_token");

        User userInfo = userService.getUserInfo(access_token);
        UserDto user = UserDto.builder()
                .token(refresh_token)
                .userId(userInfo.getUserId())
                .email(userInfo.getEmail())
                .nickname(userInfo.getNickname())
                .ci(userInfo.getCi())
                .build();

        registerService.userSave(user);

        Cookie cookie = new Cookie("access_token", access_token);
        cookie.setMaxAge(60*60*24);
        response.addCookie(cookie);

        return CommonResponse.toResponse(CommonCode.OK, response);

    }
}
