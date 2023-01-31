package com.dungzi.backend.domain.user.application;

import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserDto;
import com.dungzi.backend.global.common.error.AuthException;
import com.dungzi.backend.global.common.error.CommonErrorCode;
import com.dungzi.backend.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.dungzi.backend.domain.user.dao.UserDao;
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
    
    private final String ROLE_USER = "ROLE_USER"; //TODO : 추후 다른 권한 이름들 정리해서 추가 (공인중개사 계정 등)


    public User getUserByUuid(String uuid) throws AuthException {
        log.info("[SERVICE] getUserByUuid");
        return userDao.findByUserId(UUID.fromString(uuid))
                .orElseThrow(() -> new AuthException(CommonErrorCode.NOT_EXIST_USER));
    }

    public User updateUserEmailAuth(String univName, Boolean isEmailChecked) {
        log.info("[SERVICE] updateUserEmailAuth");
        User user = getUserFromSecurity();
        user.updateUnivEmailAuth(univName, isEmailChecked);
        return userDao.save(user);
    }

    public void setTokenCookieAndSecurityByUser(HttpServletResponse httpServletResponse, User user) {
        log.info("[SERVICE] setTokenCookieAndSecurityByUser");

        Map<String, Cookie> cookieMap = createCookieTokenByUser(user);
        setCookieTokenInResponse(httpServletResponse, cookieMap);

        Authentication authentication = jwtTokenProvider.getAuthenticationByToken(cookieMap.get("access").getValue());
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    private Map<String, Cookie> createCookieTokenByUser(User user) {
        String accessToken = jwtTokenProvider.createToken(user.getUserId().toString(), user.getRoles(), jwtTokenProvider.ACCESS_TOKEN_HEADER_NAME);
        String refreshToken = jwtTokenProvider.createToken(user.getUserId().toString(), user.getRoles(), jwtTokenProvider.REFRESH_TOKEN_HEADER_NAME);

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

    private void setCookieTokenInResponse(HttpServletResponse httpServletResponse, Map<String, Cookie> cookieMap){
        cookieMap.forEach( (key, cookie) -> {
            httpServletResponse.addCookie(cookie);
        });
    }

    @Transactional
    public User userLoginWithSignUp(User user) {
        log.info("[SERVICE] userLoginWithSignUp");

        Optional<User> userOptional = userDao.findByEmail(user.getEmail());

        if(userOptional.isEmpty()){
            log.info("userLoginWithSignUp : User not exist. Sign up and login");
            user.updateRoles(Collections.singletonList(ROLE_USER));
            return userSave(user);
        }else{
            log.info("userLoginWithSignUp : Existing user. Login");
            return userOptional.get();
        }
    }

    @Transactional
    public User userSave(UserDto requestDto) {
        return userSave(requestDto.toEntity());
    }

    @Transactional
    public User userSave(User user) {
        log.info("[SERVICE] userSave");
        return userDao.save(user);
    }

    public User getUserFromSecurity() {
        log.info("[SERVICE] getUserFromSecurity");
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}