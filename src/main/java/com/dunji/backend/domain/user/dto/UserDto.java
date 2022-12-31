package com.dunji.backend.domain.user.dto;

import lombok.Getter;

import java.util.Date;

@Getter
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

    public UserDto() {}

    public UserDto(String userId, String ci, String token, String userName, String nickname, String phoneNum, String userType, String gender,
                   boolean authCheck, String email, String profileImg, String univName, Date regDate, Date delDate) {
        this.userId = userId;
        this.ci = ci;
        this.token = token;
        this.userName = userName;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.userType = userType;
        this.gender = gender;
        this.authCheck = authCheck;
        this.email = email;
        this.profileImg = profileImg;
        this.univName = univName;
        this.regDate = regDate;
        this.delDate = delDate;
    }
}
