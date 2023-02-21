package com.dungzi.backend.domain.review.api;

import com.dungzi.backend.domain.review.application.ReviewService;
import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/")
    public CommonResponse createReview(@RequestBody ReviewRequestDto.CreateReview requestDto){
        UUID buildingId = reviewService.saveReview(requestDto);
        System.out.println(buildingId);
        return CommonResponse.toResponse(CommonCode.OK, reviewService.saveReviewDetail(requestDto.toReviewDetailEntity(buildingId)));
    }
}
