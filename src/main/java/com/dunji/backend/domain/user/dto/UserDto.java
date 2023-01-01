package com.dunji.backend.domain.user.dto;

import com.dunji.backend.domain.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserDto {

    private String userId;
    private String ci;
    private String token;
    private String userName;
    private String nickname;
    private String phoneNum;
    private String userType;
    private String gender;
    private boolean authCheck;
    private String email;
    private String profileImg;
    private String univName;
    private Date regDate;
    private Date delDate;

    public User toEntity() {
    
        return User.builder()
                .userId(userId)
                .ci(ci)
                .token(token)
                .userName(userName)
                .nickname(nickname)
                .phoneNum(phoneNum)
                .userType(userType)
                .gender(gender)
                .authCheck(false)
                .email(email)
                .profileImg(profileImg)
                .univName(univName)
                .regDate(regDate)
                .delDate(delDate)
                .build();

    }
}
