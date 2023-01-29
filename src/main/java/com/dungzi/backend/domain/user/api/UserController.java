package com.dungzi.backend.domain.user.api;

import com.dungzi.backend.domain.univ.application.UnivAuthService;
import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.univ.domain.UnivAuth;
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
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final KakaoService kakaoService;
    private final AuthService authService;
    private final EmailService emailService;
    private final UnivService univService;
    private final UnivAuthService univAuthService;

    private final String ACCESS_TOKEN = "access_token";
//    private final String REFRESH_TOKEN = "refresh_token"; //TODO : 카카오 refresh token은 저장해둘 필요 없을까? 탈퇴나 로그아웃 시

    // TODO : 인증 요청 대학교 도메인과 현제 이메일 도메인 일치 확인 로직 추가할 것 (테스트 시에도 불편할 예정)
    @PostMapping("/email-auth/send")
    public CommonResponse sendAuthEmail(@RequestBody UserRequestDto.SendEmailAuth body) throws Exception {
        log.info("[API] users/email-auth/send");
        String code = emailService.sendSimpleMessage(body.getEmail());
        log.info("이메일 전송 완료. 인증코드 : {}", code);
        UserResponseDto.SendEmailAuth response = UserResponseDto.SendEmailAuth.builder()
                .email(body.getEmail())
                .authCode(code)
                .build();
        return CommonResponse.toResponse(CommonCode.OK, response);
    }

    @PatchMapping("/email-auth")
    public CommonResponse updateUserEmailAuth(@RequestBody UserRequestDto.UpdateEmailAuth body) {
        log.info("[API] users/email-auth");
        User user = authService.getUserFromSecurity();
        Univ univ = univService.getUniv(UUID.fromString(body.getUnivId()));
        //TODO : 대학교 이메일 도메인 일치 확인 univService
        UnivAuth univAuth = univAuthService.updateUserEmailAuth(user, univ, body.getUnivEmail(), body.getIsEmailChecked());
        return CommonResponse.toResponse(CommonCode.OK, new UserResponseDto.UpdateEmailAuth(user, univAuth));
    }

    //카카오 로그인
    //TODO : swagger 작성하기
//    @Operation(summary = "채팅방 생성 api", description = "채팅방 생성을 위한 api")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "200", description = "이전에 만든방이 존재하였기에 이전의 방 조회"),
//                    @ApiResponse(responseCode = "201", description = "새로운 방 생성"),
//                    @ApiResponse(responseCode = "404", description = "해당 유저가 없음")
//            }
//    )
    @GetMapping("/login/kakao")
    public CommonResponse kakaoCallback(@RequestParam String code, HttpServletResponse httpServletResponse) throws Exception {
        log.info("[API] users/login/kakao");

        HashMap<String, String> token = kakaoService.getKakaoAccessToken(code);
        String kakao_access_token = token.get(ACCESS_TOKEN);
//        String refresh_token = token.get(REFRESH_TOKEN);

        User kakaoUser = kakaoService.getKakaoUserInfo(kakao_access_token);

        User loginUser = authService.userLoginWithSignUp(kakaoUser);

        authService.setTokenCookieAndSecurityByUser(httpServletResponse, loginUser);

        return CommonResponse.toResponse(CommonCode.OK, loginUser.getUserId());
    }

}
