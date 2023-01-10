package com.dungzi.backend.domain.user.api;

import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.application.EmailService;
import com.dungzi.backend.domain.user.application.KakaoService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.domain.user.dto.UserResponseDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final EmailService emailService;

    private final String ACCESS_TOKEN = "access_token";
//    private final String REFRESH_TOKEN = "refresh_token"; //TODO : 카카오 refresh token은 저장해둘 필요 없을까? 탈퇴나 로그아웃 시

    @PostMapping("/email-auth/send")
    public CommonResponse sendAuthEmail(@RequestBody UserRequestDto.SendEmailAuth body) throws Exception {
        log.info("[API] users/email-auth/send");
        String code = emailService.sendSimpleMessage(body.getEmail());
//        User user = authService.getUserFromSecurity();
        log.info("이메일 전송 완료. 인증코드 : {}", code);
        UserResponseDto.SendEmailAuth response = UserResponseDto.SendEmailAuth.builder()
//                .uuid(user.getUserId().toString())
                .email(body.getEmail())
                .authCode(code)
                .build();
        return CommonResponse.toResponse(CommonCode.OK, response);
    }

    @PostMapping("/email-auth")
    public CommonResponse updateUserEmailAuth(@RequestBody UserRequestDto.UpdateEmailAuth body) {
        log.info("[API] users/email-auth");
        User user = authService.updateUserEmailAuth(body.getUnivName(), body.getIsEmailChecked());
        return CommonResponse.toResponse(CommonCode.OK, user.toUpdateEmailAuthResponseDto());
    }

    //카카오 로그인
    @GetMapping("/login/kakao")
    public CommonResponse kakaoCallback(@RequestParam String code, HttpServletResponse httpServletResponse) throws Exception {
        log.info("[API] users/login/kakao");

        HashMap<String, String> token = kakaoService.getKakaoAccessToken(code);
        String kakao_access_token = token.get(ACCESS_TOKEN);
//        String refresh_token = token.get(REFRESH_TOKEN);

        User kakaoUser = kakaoService.getKakaoUserInfo(kakao_access_token);

        User loginUser = authService.userLoginWithSignUp(kakaoUser);

        authService.setTokenCookieAndSecurityByUser(httpServletResponse, loginUser);

        return CommonResponse.toResponse(CommonCode.OK, "uuid : "+loginUser.getUserId());
    }

}
