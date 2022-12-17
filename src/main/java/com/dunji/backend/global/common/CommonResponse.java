package com.dunji.backend.global.common;


import com.dunji.backend.global.common.error.CommonErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {
    private final String timeStamp = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private final int code;
    private final String message;
    private final Object data;


    public static CommonResponse toResponse(CommonCode commonCode, Object data) {
        return CommonResponse.builder()
                .code(commonCode.getCode().value())
                .message(commonCode.getMessage())
                .data(data)
                .build();
    }

    public static CommonResponse toErrorResponse(CommonErrorCode commonErrorCode) {
        return CommonResponse.builder()
                .code(commonErrorCode.getCode().value())
                .message(commonErrorCode.getMessage())
                .build();
    }

}
