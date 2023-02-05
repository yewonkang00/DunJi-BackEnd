package com.dungzi.backend.domain.univ.application;

import com.dungzi.backend.domain.univ.dao.UnivAuthDao;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.domain.univ.domain.UnivAuth;
import com.dungzi.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UnivAuthService {
    private final UnivAuthDao univAuthDao;


    @Transactional
    public UnivAuth updateUserEmailAuth(User user, Univ univ, String email, Boolean isEmailChecked) {
        log.info("[SERVICE] updateUserEmailAuth");
//        User user = getUserFromSecurity();

        Optional<UnivAuth> univAuthOp = univAuthDao.findByUser(user);
        UnivAuth univAuth = createOrUpdateUnivAuth(univAuthOp, user, univ, email, isEmailChecked);

        return univAuthDao.save(univAuth);
    }

    private UnivAuth createOrUpdateUnivAuth(Optional<UnivAuth> univAuthOp, User user, Univ univ, String email, Boolean isEmailChecked) {
        if(univAuthOp.isPresent()){
            UnivAuth univAuth = univAuthOp.get();
            univAuth.updateUnivAuth(univ, isEmailChecked);
            return univAuth;
        }
        else {
            // TODO 고려사항 : 파라미터를 묶은 Controller<->Service 간 model dto클래스를 만들면 이 코드를 그쪽 메서드로 넘길 수 있음
            return UnivAuth.builder()
                    .user(user)
                    .univ(univ)
                    .email(email)
                    .isChecked(isEmailChecked)
                    .build();
        }
    }

}
