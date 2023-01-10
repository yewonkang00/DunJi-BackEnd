package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthException extends RuntimeException {
    private final Code code;
}
