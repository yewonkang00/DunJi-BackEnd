package com.dungzi.backend.domain.user.domain;

import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.user.dto.UserDto;
import com.dungzi.backend.domain.user.dto.UserResponseDto;
import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column( nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID userId;

    @Column(nullable = false)
    private String nickname; //kakao 필수 동의 항목

    @Column(nullable = false)
    private String email; //kakao 필수 동의 항목

    //    @Column(nullable = false)
    private String ci;

    private String profileImg; //kakao 선택 동의 항목
    private String phoneNum;
    private String userType;
    private String gender;
    private String univName;
    private Boolean authCheck; //TODO : 컬럼명 변경 고려 - isUnivEmailChecked

    //    @Temporal(TemporalType.TIMESTAMP)
    private Date delDate;

//    @Column(nullable = false)
//    private String token;

    //    @Column(nullable = false)
//    private String userName;



    public void updateUnivEmailAuth(String univName, Boolean isChecked) {

        this.univName = univName;
        this.authCheck = isChecked;
    }

    public void updateRoles(List<String> roles) {
        this.roles = roles;
    }



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

    public UserResponseDto.UpdateEmailAuth toUpdateEmailAuthResponseDto() {
        return  UserResponseDto.UpdateEmailAuth.builder()
                .uuid(this.getUserId().toString())
                .univName(this.getUnivName())
                .emailAuthCheck(this.getAuthCheck())
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
