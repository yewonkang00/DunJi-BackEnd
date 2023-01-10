package com.dungzi.backend.global.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Setter
@Getter //mapper 사용을 위해 필요
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    public ZonedDateTime regDate;

    @PrePersist
    public void prePersist() {
        this.regDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    }

//    @PreUpdate
//    public void preUpdate() {
//        this.modifiedTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
//    }
}
