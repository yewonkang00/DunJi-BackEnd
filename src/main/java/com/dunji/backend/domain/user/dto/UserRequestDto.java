package com.dunji.backend.domain.user.dto;

import lombok.Builder;
import lombok.Data;

public class UserRequestDto {

    @Data
    public static class UpdateEmailAuth {
        private String univName;
        private Boolean isEmailChecked;
    }

    @Data
    public static class SendEmailAuth {
        private String email;
    }
}
