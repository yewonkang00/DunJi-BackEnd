package com.dunji.backend.domain.user.application;

import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.domain.user.dto.UserDto;
import com.dunji.backend.global.common.error.AuthException;
import com.dunji.backend.global.common.error.CommonErrorCode;
import com.dunji.backend.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.dunji.backend.domain.user.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;
    
    private final String ROLE_USER = "ROLE_USER"; //TODO : 추후 다른 권한 이름들 정리해서 추가


    public User getUserByUuid(String uuid) throws AuthException {
        return userDao.findByUserId(UUID.fromString(uuid))
                .orElseThrow(() -> new AuthException(CommonErrorCode.NOT_EXIST_USER));
    }

    public Map<String, Cookie> createCookie(String accessToken, String refreshToken) {
        Map<String, Cookie> cookieMap = new HashMap();
        cookieMap.put("access", new Cookie(jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME, accessToken));
        cookieMap.get("access").setMaxAge(jwtTokenProvider.ACCESS_COOKIE_MAX_AGE);

        cookieMap.put("refresh", new Cookie(jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME, refreshToken));
        cookieMap.get("refresh").setMaxAge(jwtTokenProvider.REFRESH_COOKIE_MAX_AGE);

        cookieMap.forEach( (key, cookie) -> {
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            //cookie.setSecure(true); //https 상에서만 동작
        });

        return cookieMap;
    }

    public void setCookieTokenByUser(HttpServletResponse httpServletResponse, User user){
        String jwtAccessToken = jwtTokenProvider.createToken(user.getUserId().toString(), user.getRoles(), jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        String jwtRefreshToken = jwtTokenProvider.createToken(user.getUserId().toString(), user.getRoles(), jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);

        Map<String, Cookie> cookieMap = this.createCookie(jwtAccessToken, jwtRefreshToken);
        cookieMap.forEach( (key, cookie) -> {
            httpServletResponse.addCookie(cookie);
        });

    }

    @Transactional
    public User userLoginWithSignUp(User user) {
        Optional<User> userOptional = userDao.findByEmail(user.getEmail());

        if(userOptional.isEmpty()){
            log.info("[SERVICE] userLoginWithSignUp : User not exist. Sign up and login");
            user.setRoles(Collections.singletonList(ROLE_USER));
            return userSave(user);
        }else{
            log.info("[SERVICE] userLoginWithSignUp : Existing user. Login");
            return userOptional.get();
        }
    }

    @Transactional
    public User userSave(UserDto requestDto) {
        return userSave(requestDto.toEntity());
    }

    @Transactional
    public User userSave(User user) {
        return userDao.save(user);
    }

}