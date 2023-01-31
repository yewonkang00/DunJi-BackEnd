package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements Code {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid."), //401
    GUEST_USER(HttpStatus.UNAUTHORIZED, "Request from guest user. Token is empty."), //401
    NOT_EXIST_USER(HttpStatus.UNAUTHORIZED, "User of this uuid is not exist."), //401
    KAKAO_FAILED(HttpStatus.CONFLICT, "카카오관련 처리를 실행할 수 없습니다.") //500
    ;

    private HttpStatus code;
    private String  message;


    CommonErrorCode(HttpStatus code, String message) {
        this.code=code;
        this.message=message;
    }
}
