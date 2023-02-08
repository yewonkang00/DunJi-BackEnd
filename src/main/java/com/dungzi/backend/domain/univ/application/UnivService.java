package com.dungzi.backend.domain.univ.application;

import com.dungzi.backend.domain.univ.dao.UnivDao;
import com.dungzi.backend.domain.univ.domain.Univ;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.error.AuthErrorCode;
import com.dungzi.backend.global.common.error.AuthException;
import com.dungzi.backend.global.common.error.UnivErrorCode;
import com.dungzi.backend.global.common.error.UnivException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UnivService {

    private final UnivDao univDao;

    public boolean isUnivDomain(String email, String univId) {
        String domain = email.substring(email.lastIndexOf("@")+1);
        Univ univ = univDao.findById(univId)
                .orElseThrow(() -> new UnivException(UnivErrorCode.UNIV_NOT_FOUND));
        return univ.isDomain(domain); //TODO : 예외 핸들링하기!!
    }

    public Univ getUniv(String univId) {
        log.info("[SERVICE] getUniv");
        return univDao.findById(univId)
                .orElseThrow(() -> new UnivException(CommonCode.NOT_FOUND)); //TODO : 예외처리 handler로 핸들링하기!!!
    }

    public List<Univ> getUnivAll() {
        log.info("[SERVICE] getUnivAll");
        return univDao.findAll();
    }

    public Univ saveUniv(Univ univ) {
        return univDao.save(univ);
    }
}
