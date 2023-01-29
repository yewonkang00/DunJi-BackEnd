package com.dungzi.backend.domain.univ.application;

import com.dungzi.backend.domain.univ.dao.UnivDao;
import com.dungzi.backend.domain.univ.domain.Univ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UnivService {

    private final UnivDao univDao;

    public Univ getUniv(UUID univId) {
        log.info("[SERVICE] getUniv");
        return univDao.findById(univId)
                .orElseThrow(); //TODO : 예외처리
    }

    public List<Univ> getUnivAll() {
        log.info("[SERVICE] getUnivAll");
        return univDao.findAll();
    }

    public Univ saveUniv(Univ univ) {
        return univDao.save(univ);
    }
}
