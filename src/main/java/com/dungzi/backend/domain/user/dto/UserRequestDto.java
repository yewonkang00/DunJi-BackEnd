package com.dungzi.backend.domain.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserRequestDto {

    @Data
    public static class UpdateEmailAuth {
        private String univId;
        private String univEmail;
        private Boolean isEmailChecked;
    }

    @Data
    public static class SendEmailAuth {
        private String email;
    }

    @Data
    public static class SignUpByKakao {
        @NotBlank
        private String kakaoAccessToken;

        @NotNull
        private Boolean isUnivAuth;

        private String univId;
        private String univEmail;
        private String nickname;
    }
}
