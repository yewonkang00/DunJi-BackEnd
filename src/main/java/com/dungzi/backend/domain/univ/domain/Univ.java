package com.dungzi.backend.domain.univ.domain;

import com.dungzi.backend.global.common.response.code.UnivErrorCode;
import com.dungzi.backend.global.common.response.exception.UnivException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Univ {
    @Id
    @Column(nullable = false)
    private String univId;

    private String univName;

    private String emailDomain;

    public void checkDomain(String domain) {
        if(this.getEmailDomain().equals(domain)){
            log.info("univ email domain is correct");
        }
        else{
            throw new UnivException(UnivErrorCode.UNIV_DOMAIN_MISMATCH);
        }
    }
}
