package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;
import com.dungzi.backend.global.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DefaultException.class)
    public ResponseEntity<Object> handleAuthException(DefaultException e){
        Code code = e.getCode();
        return handleExceptionInternal(code);
    }

    private ResponseEntity<Object> handleExceptionInternal(Code code){
        CommonResponse errorResponse = CommonResponse.toErrorResponse(code);
        return ResponseEntity
                .status(code.getCode())
                .body(errorResponse);
    }
}
