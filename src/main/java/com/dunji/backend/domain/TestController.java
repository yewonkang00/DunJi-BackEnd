package com.dunji.backend.domain;

import com.dunji.backend.domain.user.application.UserService;
import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.global.common.CommonCode;
import com.dunji.backend.global.common.CommonResponse;
import com.dunji.backend.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {
    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @GetMapping("/hello")
    public CommonResponse hello() {
        log.info("[API] test/hello");
        return CommonResponse.toResponse(CommonCode.OK, "hello");
    }

    @GetMapping("/checkToken/access")
//    public CommonResponse checkAccessToken(HttpServletRequest httpServletRequest) { //@CookieValue("x-access-token") String accessToken
    public CommonResponse checkAccessToken(HttpServletRequest httpServletRequest) {
        String uuid = jwtTokenProvider.getUserPKByServlet(httpServletRequest, jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        User user = userService.getUserByUuid(uuid);
        return CommonResponse.toResponse(CommonCode.OK, "access token 사용자 닉네임 : "+user.getNickname()+", 이메일 : "+user.getEmail());
    }
}
