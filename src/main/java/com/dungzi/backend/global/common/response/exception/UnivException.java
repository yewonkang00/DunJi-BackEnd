package com.dungzi.backend.global.common.response.exception;

import com.dungzi.backend.global.common.response.code.Code;

public class UnivException extends DefaultException {
    public UnivException(Code code) {
        super(code);
    }
}
