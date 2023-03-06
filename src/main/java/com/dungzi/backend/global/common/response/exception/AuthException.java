package com.dungzi.backend.global.common.response.exception;

import com.dungzi.backend.global.common.response.code.Code;


public class AuthException extends DefaultException {
    public AuthException(Code code) {
        super(code);
    }
}
