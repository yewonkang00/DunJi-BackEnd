package com.dungzi.backend.domain.user.domain;

import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String ci;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String nickname;

    private String phoneNum;
    private String userType;
    private String gender;
    private boolean authCheck;
    private String email;
    private String profileImg;
    private String univName;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date delDate;

    @Builder
    public User(String userId, String ci, String token, String userName, String nickname, String phoneNum, String userType, String gender,
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
