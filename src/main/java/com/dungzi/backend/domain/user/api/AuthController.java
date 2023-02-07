package com.dungzi.backend.domain.user.api;

import com.dungzi.backend.domain.univ.application.UnivAuthService;
import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.application.EmailService;
import com.dungzi.backend.domain.user.application.KakaoService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.domain.user.dto.UserResponseDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import com.dungzi.backend.global.common.error.AuthException;
import com.dungzi.backend.global.common.error.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final KakaoService kakaoService;
    private final AuthService authService;
    private final EmailService emailService;
    private final UnivService univService;
    private final UnivAuthService univAuthService;

    private final String ACCESS_TOKEN = "access_token";
//    private final String REFRESH_TOKEN = "refresh_token"; //TODO : 카카오 refresh token은 저장해둘 필요 없을까? 탈퇴나 로그아웃 시

    // TODO : 인증 요청 대학교 도메인과 현제 이메일 도메인 일치 확인 로직 추가할 것 (테스트 시에도 불편할 예정)
    @GetMapping("/code")
    public CommonResponse sendAuthEmail(@RequestParam String email, @RequestParam(value = "univ") String univId) throws Exception {
        log.info("[API] auth/email-auth/send");
        univService.checkUnivDomain(email, univId);
        String code = emailService.sendSimpleMessage(email);
        log.info("이메일 전송 완료. 인증코드 : {}", code);
        UserResponseDto.SendEmailAuth response = UserResponseDto.SendEmailAuth.builder()
                .email(email)
                .authCode(code)
                .build();
        return CommonResponse.toResponse(CommonCode.OK, response);
    }

    @PostMapping("/kakao")
    public CommonResponse signUpByKakao(@RequestBody UserRequestDto.SignUpByKakao requestDto) {
        log.info("[API] auth/kakao");

        // TODO : 예외처리 Handler 사용하여 중복코드 개선하기
        User kakaoUser;
        try {
            kakaoUser = kakaoService.getKakaoUserInfo(requestDto.getKakaoAccessToken());
        } catch (Exception e) {
            log.warn("getKakaoUserInfo failed");
            e.printStackTrace();
            return CommonResponse.toErrorResponse(AuthErrorCode.KAKAO_FAILED);
        }

        // TODO : 형식적 유효성 검사가 원래 컨트롤러에서 하는 일이니 여기서 Optional로 하는 게 맞을 듯 (필수값이 아니니까 여기서 isPresent를 확인하진 않아도 되지 않을까?
        Optional<String> nicknameOp = Optional.ofNullable(requestDto.getNickname());

        //회원가입
        User newUser = authService.signUpByKakao(kakaoUser, nicknameOp);

        //이메일 인증 정보 저장
        if(requestDto.getIsUnivAuth()){
            Univ univ = univService.getUniv(requestDto.getUnivId());
            univAuthService.createUnivAuth(newUser, univ, requestDto.getUnivEmail(), true);
        }

        return CommonResponse.toResponse(CommonCode.OK, UserResponseDto.SignUpByKakao.toDto(newUser));
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
    @GetMapping("/kakao")
    //TODO : 회원가입/로그인 로직 분리, 변경
    public CommonResponse kakaoCallback(@RequestParam String code, HttpServletResponse httpServletResponse) {
        log.info("[API] auth/kakao");

        HashMap<String, String> token = kakaoService.getKakaoAccessToken(code);
        String kakao_access_token = token.get(ACCESS_TOKEN);
//        String refresh_token = token.get(REFRESH_TOKEN);


        // TODO : 예외처리 Handler 사용하여 중복코드 개선하기
        User kakaoUser;
        try {
            kakaoUser = kakaoService.getKakaoUserInfo(kakao_access_token);
        } catch (Exception e) {
            log.info("getKakaoUserInfo failed");
            e.printStackTrace();
            return CommonResponse.toErrorResponse(AuthErrorCode.KAKAO_FAILED);
        }

        // TODO : 코드리뷰 필요 - 회원 확인 코드가 Controller와 Service 중 어디에 위치하는 것이 좋을까?
        // Conroller에 위치할 경우 : login 메서드와 회원확인 메서드 분리. if-else문 사용
        // Service에 위치할 경우 : login 메서드 안에 회원확인 로직 포함. 예외 throw 하여 try-catch문 사용
//        boolean isRegistered = authService.isRegistered(kakaoUser);
        UserResponseDto.KakaoLogin responseDto = new UserResponseDto.KakaoLogin();
//        responseDto.setIsUser(isRegistered);

        try {
            User loginUser = authService.login(kakaoUser);
            authService.setCookieTokenInResponse(httpServletResponse, loginUser); //위로 합치기
            responseDto.setUuid(loginUser.getUserId());
            responseDto.setIsUser(true);
        }
        catch(AuthException authException) {
            if(authException.getCode() == AuthErrorCode.NOT_EXIST_USER){
                responseDto.setKakaoAccessToken(kakao_access_token);
                responseDto.setIsUser(false);
            }
        }

        return CommonResponse.toResponse(CommonCode.OK, responseDto);
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
