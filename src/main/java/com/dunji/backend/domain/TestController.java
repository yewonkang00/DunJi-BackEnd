package com.dunji.backend.domain;

import com.dunji.backend.global.common.CommonCode;
import com.dunji.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/hello")
    public CommonResponse hello() {
        log.info("[API] test/hello");
        return CommonResponse.toResponse(CommonCode.OK, "hello");
    }
}
