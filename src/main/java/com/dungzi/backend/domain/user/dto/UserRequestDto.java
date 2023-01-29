package com.dungzi.backend.domain.user.dto;

import lombok.Data;

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
}
