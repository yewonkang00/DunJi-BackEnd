package com.dungzi.backend.domain.user.dto;

import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

public class UserUtilResponseDto {

    @Builder
    @Data
    public static class GetUserProfile {
        private UUID userId;
        private String nickname;
        private String profileImg;
        private String email;
        private String univName;

        public static GetUserProfile toDto(User user, UnivAuth univAuth){
            return GetUserProfile.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .profileImg(user.getProfileImg())
                    .univName(univAuth.getUniv().getUnivName())
                    .build();
        }
    }

    @Builder
    @AllArgsConstructor
    @Data
    public static class UpdateEmailAuth {
        private UUID uuid;
        private String univName;
        private Boolean emailAuthCheck;

        //TODO : 추후 modelmapper 사용 고려
        public static UpdateEmailAuth toDto(User user, UnivAuth univAuth) {
            return UpdateEmailAuth.builder()
                    .uuid(user.getUserId())
                    .univName(univAuth.getUniv().getUnivName())
                    .emailAuthCheck(univAuth.isChecked())
                    .build();
        }

    }

}
