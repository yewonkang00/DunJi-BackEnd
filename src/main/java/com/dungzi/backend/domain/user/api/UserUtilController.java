package com.dungzi.backend.domain.user.api;

import com.dungzi.backend.domain.univ.application.UnivAuthService;
import com.dungzi.backend.domain.univ.application.UnivService;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.domain.user.dto.UserAuthResponseDto;
import com.dungzi.backend.domain.user.dto.UserUtilResponseDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import com.dungzi.backend.global.common.error.AuthErrorCode;
import com.dungzi.backend.global.common.error.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserUtilController {
    private final AuthService authService;
    private final UnivService univService;
    private final UnivAuthService univAuthService;

    @GetMapping("/profile")
    public CommonResponse getUserProfile() {
        log.info("[API] users/profile");
        User user = authService.getUserFromSecurity();
        UnivAuth univAuth = univAuthService.getUnivAuthByUser(user);
        return CommonResponse.toResponse(CommonCode.OK, UserUtilResponseDto.GetUserProfile.toDto(user, univAuth));
    }

    @PutMapping("/univs")
    public CommonResponse updateUserEmailAuth(@RequestBody UserRequestDto.UpdateEmailAuth requestDto) {
        log.info("[API] users/univs");
        User user = null;
        try {
            user = authService.getUserFromSecurity();
        }
        catch (AuthException authException){
            if(authException.getCode() == CommonCode.UNAUTHORIZED){
                return CommonResponse.toErrorResponse(AuthErrorCode.GUEST_USER);
            }
        }

        Univ univ = univService.getUniv(requestDto.getUnivId());
        univService.checkUnivDomain(requestDto.getUnivEmail(), univ);

        UnivAuth univAuth = univAuthService.updateUserEmailAuth(user, univ, requestDto.getUnivEmail(), requestDto.getIsEmailChecked());
        return CommonResponse.toResponse(CommonCode.OK, UserUtilResponseDto.UpdateEmailAuth.toDto(user, univAuth));
    }
}
