package com.dunji.backend.global.common;

import javax.annotation.Generated;
import org.springframework.http.HttpStatus;

public interface Code {
    HttpStatus getCode();
    String getMessage();
}
