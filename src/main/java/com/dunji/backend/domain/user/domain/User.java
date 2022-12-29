package com.dunji.backend.domain.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
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
}
