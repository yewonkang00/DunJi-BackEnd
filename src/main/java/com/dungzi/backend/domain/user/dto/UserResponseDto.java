package com.dungzi.backend.domain.user.dto;

import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class UserResponseDto {

    @Builder
    @AllArgsConstructor
    @Data
    public static class UpdateEmailAuth {
        private String uuid;
        private String univName;
        private Boolean emailAuthCheck;

        //TODO : 추후 modelmapper 사용 고려
        public UpdateEmailAuth(User user, UnivAuth univAuth) {
            this.uuid = user.getUserId().toString();
            this.univName = univAuth.getUniv().getUnivName();
            this.emailAuthCheck = univAuth.isChecked();
//            this.univName = user.getUnivName();
//            this.emailAuthCheck = user.getAuthCheck();
        }

    }

    @Builder
    @Data
    public static class SendEmailAuth {
//        private String uuid;
        private String email;
        private String authCode;
    }
}
