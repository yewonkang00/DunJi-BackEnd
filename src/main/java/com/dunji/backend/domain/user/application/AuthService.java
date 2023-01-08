package com.dunji.backend.domain.user.application;

import com.dunji.backend.domain.user.domain.User;
import com.dunji.backend.domain.user.dto.UserDto;
import com.dunji.backend.global.common.error.AuthException;
import com.dunji.backend.global.common.error.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.dunji.backend.domain.user.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthService {
    private final UserDao userDao;
    
    private final String ROLE_USER = "ROLE_USER"; //TODO : 추후 다른 권한 이름들 정리해서 추가

    public User getUserByUuid(String uuid) throws AuthException {
        return userDao.findByUserId(UUID.fromString(uuid))
                .orElseThrow(() -> new AuthException(CommonErrorCode.NOT_EXIST_USER));
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