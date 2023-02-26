package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ValidErrorCode implements Code {
    REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다."), //400
    ;

    @Override
    public String toString() {
        return super.toString();
    }

    private HttpStatus code;
    private String  message;

    ValidErrorCode(HttpStatus code, String message) {
        this.code=code;
        this.message=message;
    }
}
