package com.dungzi.backend.domain.review.api;

import com.dungzi.backend.domain.review.application.ReviewService;
import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "후기 생성 api", description = "후기 생성을 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 후기 등록 완료")
            }
    )
    @PostMapping("/")
    public CommonResponse createReview(@RequestBody ReviewRequestDto.CreateReview requestDto){
        UUID buildingId = reviewService.saveReview(requestDto);
        System.out.println(buildingId);
        return CommonResponse.toResponse(CommonCode.OK, reviewService.saveReviewDetail(requestDto.toReviewDetailEntity(buildingId)));
    }
}
