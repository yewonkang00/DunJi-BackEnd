package com.dunji.backend.domain.user.domain;

import com.dunji.backend.domain.user.dto.UserDto;
import com.dunji.backend.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String nickname; //kakao 필수 동의 항목

    @Column(nullable = false)
    private String email; //kakao 필수 동의 항목

    //    @Column(nullable = false)
    private String ci;

    private String profileImg; //kakao 필수 동의 항목
    private String phoneNum;
    private String userType;
    private String gender;
    private Boolean authCheck;
    private String univName;

//    @Temporal(TemporalType.TIMESTAMP)
    private Date delDate;

//    @Column(nullable = false)
//    private String token;

//    @Column(nullable = false)
//    private String userName;


    ///-- toDto method --///
    public UserDto toUserDto() {
        //TODO : 추후 modelmapper 사용 고려
        return UserDto.builder()
                .nickname(this.getNickname())
                .email(this.getEmail())
                .ci(this.getCi())
                .profileImg(this.getProfileImg())
                .phoneNum(this.getPhoneNum())
                .userType(this.getUserType())
                .gender(this.getGender())
                .authCheck(this.getAuthCheck())
                .univName(this.getUnivName())
                .regDate(this.getRegDate())
                .delDate(this.getDelDate())
                .build();
    }



    //////////-- set user roles(Authentication) : implements UserDetails --//////////
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.getUserId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

}
