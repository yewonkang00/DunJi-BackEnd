package com.dungzi.backend.global.common.response.exception;

import com.dungzi.backend.global.common.response.code.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DefaultException extends RuntimeException {
    private final Code code;
}
