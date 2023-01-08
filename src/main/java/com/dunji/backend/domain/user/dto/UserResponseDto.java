package com.dunji.backend.domain.user.dto;

import lombok.Builder;
import lombok.Data;

public class UserResponseDto {

    @Builder
    @Data
    public static class UpdateEmailAuth {
        private String uuid;
        private String univName;
        private Boolean emailAuthCheck;
    }

    @Builder
    @Data
    public static class SendEmailAuth {
//        private String uuid;
        private String email;
        private String authCode;
    }
}
