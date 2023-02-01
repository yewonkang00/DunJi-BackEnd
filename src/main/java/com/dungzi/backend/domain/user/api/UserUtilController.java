package com.dungzi.backend.domain.user.api;

import com.dungzi.backend.domain.univ.application.UnivAuthService;
import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.domain.user.dto.UserResponseDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserUtilController {
    private final AuthService authService;
    private final UnivService univService;
    private final UnivAuthService univAuthService;

    @PatchMapping("/email-auth")
    public CommonResponse updateUserEmailAuth(@RequestBody UserRequestDto.UpdateEmailAuth body) {
        log.info("[API] users/email-auth");
        User user = authService.getUserFromSecurity();
        Univ univ = univService.getUniv(UUID.fromString(body.getUnivId()));
        // TODO : 대학교 이메일 도메인 일치 확인 univService
        UnivAuth univAuth = univAuthService.updateUserEmailAuth(user, univ, body.getUnivEmail(), body.getIsEmailChecked());
        return CommonResponse.toResponse(CommonCode.OK, new UserResponseDto.UpdateEmailAuth(user, univAuth));
    }
}
