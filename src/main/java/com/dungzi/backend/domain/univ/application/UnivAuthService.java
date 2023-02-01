package com.dungzi.backend.domain.univ.application;

import com.dungzi.backend.domain.univ.dao.UnivAuthDao;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UnivAuthService {
    private final UnivAuthDao univAuthDao;


    public UnivAuth updateUserEmailAuth(User user, Univ univ, String email, Boolean isEmailChecked) {
        log.info("[SERVICE] updateUserEmailAuth");
//        User user = getUserFromSecurity();

        Optional<UnivAuth> univAuthOp = univAuthDao.findByUser(user);
        UnivAuth univAuth;

        if(univAuthOp.isPresent()){
            univAuth = univAuthOp.get();
            univAuth.updateUnivAuth(univ, isEmailChecked);
        }
        else {
            // TODO 고려사항 : 파라미터를 묶은 Controller<->Service 간 model dto클래스를 만들면 이 코드를 그쪽 메서드로 넘길 수 있음
            univAuth = UnivAuth.builder()
                    .user(user)
                    .univ(univ)
                    .email(email)
                    .isChecked(isEmailChecked)
                    .build();
        }

        return univAuthDao.save(univAuth);
    }

}
