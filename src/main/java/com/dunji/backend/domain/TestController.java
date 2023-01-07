package com.dunji.backend.domain;

import com.dunji.backend.domain.user.application.RegisterService;
import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.global.common.CommonCode;
import com.dunji.backend.global.common.CommonResponse;
import com.dunji.backend.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {
    private final JwtTokenProvider jwtTokenProvider;

    private final RegisterService registerService;

    @GetMapping("/hello")
    public CommonResponse hello() {
        log.info("[API] test/hello");
        return CommonResponse.toResponse(CommonCode.OK, "hello");
    }

    @GetMapping("/checkToken/{tokenType}")
//    public CommonResponse checkAccessToken(HttpServletRequest httpServletRequest) { //@CookieValue("x-access-token") String accessToken
    public CommonResponse checkToken(@PathVariable String tokenType, HttpServletRequest httpServletRequest) {
        log.info("[API] test/checkToken");

        String headerName = "";
        if(tokenType.equals("access")){
            headerName = jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME;
        }else if(tokenType.equals("refresh")){
            headerName = jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME;
        }

        String message;
        User user;
        //이 요청 시 액세스 토큰이 만료되어서 필터에서 새 액세스 토큰을 저장했어도 이 serveletReqest 에는 구토큰이 저장되어있음
        if(jwtTokenProvider.isTokenValidByServlet(httpServletRequest, headerName)){
            String uuid = jwtTokenProvider.getUserPKByServlet(httpServletRequest, headerName);
            user = registerService.getUserByUuid(uuid);
            message = tokenType+" token 사용자 닉네임 : "+user.getNickname()+", 이메일 : "+user.getEmail();
        }else{
            message = "요청 시 보내신 토큰은 만료되었습니다.";
        }

        return CommonResponse.toResponse(CommonCode.OK, message);
    }
}
