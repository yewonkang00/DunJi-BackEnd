package com.dungzi.backend.global.common;

import org.springframework.http.HttpStatus;

public interface Code {
    HttpStatus getCode();
    String getMessage();
}
