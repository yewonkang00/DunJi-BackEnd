package com.dunji.backend.global.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonCode implements Code{
    OK(HttpStatus.OK, "정상 처리"),
    CREATED(HttpStatus.CREATED,"새로운 리소스 생성"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"잘못된 요청"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증되지 않은 요청"),
    FORBIDDEN(HttpStatus.FORBIDDEN,"접근 권한 부족"),
    NOT_FOUND(HttpStatus.NOT_FOUND,"요청 리소스 존재하지 않음"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버에 오류 발생");


    private HttpStatus code;
    private String  message;

    CommonCode(HttpStatus code, String message) {
        this.code=code;
        this.message=message;
    }


}
