package com.dunji.backend.global.common.error;

import com.dunji.backend.global.common.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements Code {
    ;
    private HttpStatus code;
    private String  message;
}
