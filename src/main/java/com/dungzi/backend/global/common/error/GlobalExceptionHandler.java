package com.dungzi.backend.global.common.error;

import com.dungzi.backend.global.common.Code;
import com.dungzi.backend.global.common.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthException(AuthException e){
        Code code = e.getCode();
        return handleExceptionInternal(code);
    }

    @ExceptionHandler(UnivException.class)
    public ResponseEntity<Object> handleUnivException(UnivException e){
        Code code = e.getCode();
        return handleExceptionInternal(code);
    }


    private ResponseEntity<Object> handleExceptionInternal(Code code){
        CommonResponse errorResponse = CommonResponse.toErrorResponse(code);
        return ResponseEntity
                .status(code.getCode())
                .body(errorResponse); //errorResonse 를 body로 줌
    }
}
