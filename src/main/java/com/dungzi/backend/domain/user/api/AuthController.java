package com.dungzi.backend.domain.user.api;

import com.dungzi.backend.domain.univ.application.UnivAuthService;
import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.application.EmailService;
import com.dungzi.backend.domain.user.application.KakaoService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.domain.user.dto.UserAuthResponseDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import com.dungzi.backend.global.common.error.AuthException;
import com.dungzi.backend.global.common.error.AuthErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final String LOGIN_SUCCESS_REDIRECT_URL = "http://localhost:3000";
    private final String LOGIN_FAIL_REDIRECT_URL = "http://localhost:3000/login/kakao";
    private final String LOGIN_FAIL_KAKAO_TOKEN_HEADER = "kakao-access-token";

    private final KakaoService kakaoService;
    private final AuthService authService;
    private final EmailService emailService;
    private final UnivService univService;
    private final UnivAuthService univAuthService;

    private final String ACCESS_TOKEN = "access_token";
//    private final String REFRESH_TOKEN = "refresh_token";

    @Operation(summary = "이메일 인증코드 api", description = "이메일 인증코드 전송 요청 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
                    @ApiResponse(responseCode = "500", description = "카카오 관련 에러")
            }
    )
    @GetMapping("/code")
    public CommonResponse sendAuthEmail(@RequestParam String email, @RequestParam(value = "univ") String univId) throws Exception {
        log.info("[API] auth/code");
        univService.checkUnivDomain(email, univId);
        String code = emailService.sendSimpleMessage(email);
        log.info("이메일 전송 완료. 인증코드 : {}", code);
        UserAuthResponseDto.SendEmailAuth response = UserAuthResponseDto.SendEmailAuth.builder()
                .email(email)
                .authCode(code)
                .build();
        return CommonResponse.toResponse(CommonCode.OK, response);
    }

    @Operation(summary = "카카오 회원가입 api", description = "카카오 계정 연동 회원가입")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "회원 가입 성공"),
                    @ApiResponse(responseCode = "500", description = "카카오 관련 에러")
            }
    )
    @PostMapping("/kakao")
    public CommonResponse signUpByKakao(@RequestBody UserRequestDto.SignUpByKakao requestDto) {
        log.info("[API] auth/kakao");

        User kakaoUser = kakaoService.getKakaoUserInfo(requestDto.getKakaoAccessToken());

        // TODO : 형식적 유효성 검사가 원래 컨트롤러에서 하는 일이니 여기서 Optional로 하는 게 맞을 듯 (필수값이 아니니까 여기서 isPresent를 확인하진 않아도 되지 않을까?
        Optional<String> nicknameOp = Optional.ofNullable(requestDto.getNickname());

        //회원가입
        User newUser = authService.signUpByKakao(kakaoUser, nicknameOp);

        //이메일 인증 정보 저장
        if(requestDto.getIsUnivAuth()){
            Univ univ = univService.getUniv(requestDto.getUnivId());
            univAuthService.createUnivAuth(newUser, univ, requestDto.getUnivEmail(), true);
        }

        return CommonResponse.toResponse(CommonCode.OK, UserAuthResponseDto.SignUpByKakao.toDto(newUser));
    }

    //카카오 로그인
    @Operation(summary = "카카오 로그인 api", description = "카카오 계정 연동 로그인")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "회원 가입된 유저. 로그인 성공"),
                    @ApiResponse(responseCode = "200", description = "가입되지 않은 유저인 경우 카카오 액세스 토큰 전달"),
                    @ApiResponse(responseCode = "500", description = "카카오 관련 에러")
            }
    )
    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code, HttpServletResponse httpServletResponse) throws IOException {
        log.info("[API] auth/kakao");

        HashMap<String, String> token = kakaoService.getKakaoAccessToken(code);
        String kakao_access_token = token.get(ACCESS_TOKEN);

        User kakaoUser = kakaoService.getKakaoUserInfo(kakao_access_token);

        try {
            User loginUser = authService.login(kakaoUser);
            authService.setCookieTokenInResponse(httpServletResponse, loginUser); //위로 합치기
            httpServletResponse.sendRedirect(LOGIN_SUCCESS_REDIRECT_URL);
        }
        catch(AuthException authException) {
            if(authException.getCode() == AuthErrorCode.NOT_EXIST_USER){
                httpServletResponse.addHeader(LOGIN_FAIL_KAKAO_TOKEN_HEADER, kakao_access_token);
                httpServletResponse.sendRedirect(LOGIN_FAIL_REDIRECT_URL);
            }
        }

    }



    ///////////TODO : 추후 제거
    @GetMapping("/login/kakao/login-with-sign-up")
    public CommonResponse kakaoCallbackLoginWithSignUp(@RequestParam String code, HttpServletResponse httpServletResponse) throws Exception {
        log.info("[API] auth/login/kakao/login-with-sign-up");

        HashMap<String, String> token = kakaoService.getKakaoAccessToken(code);
        String kakao_access_token = token.get(ACCESS_TOKEN);
//        String refresh_token = token.get(REFRESH_TOKEN);

        User kakaoUser = kakaoService.getKakaoUserInfo(kakao_access_token);

        User loginUser = authService.userLoginWithSignUp(kakaoUser);

        authService.setTokenCookieAndSecurityByUser(httpServletResponse, loginUser);

        return CommonResponse.toResponse(CommonCode.OK, loginUser.getUserId());
    }
}
