package com.dungzi.backend.global.common.response.exception;

import com.dungzi.backend.global.common.response.code.Code;

public class ValidException extends DefaultException {
    public ValidException(Code code) {
        super(code);
    }
}
