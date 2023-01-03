package com.dunji.backend.domain.user.application;

import com.dunji.backend.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.dunji.backend.domain.user.dao.UserDao;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterService {
    private final UserDao userDao;

    @Transactional
    public void userSave(UserDto requestDto) {
        userDao.save(requestDto.toEntity());
    }

}