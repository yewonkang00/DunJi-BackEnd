package com.dungzi.backend.domain.review.dto;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ReviewDetailResponseDto {
    private UUID reviewDetailId;

    private String userNickname;

    private String content;

    private String address;

    private String addressDetail;

    private String period;

    private String regDate;

    private List<String> image;

    private float totalRate;

    static public String zonedDateTimeToDateTime(final ZonedDateTime zdt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.M.dd. a hh:mm");
        return zdt.format(formatter);
    }



}
