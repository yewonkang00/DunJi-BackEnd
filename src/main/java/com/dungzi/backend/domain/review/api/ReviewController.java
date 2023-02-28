package com.dungzi.backend.domain.review.api;

import com.dungzi.backend.domain.review.application.ReviewService;
import com.dungzi.backend.domain.review.dto.ReviewDetailResponseDto;
import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
import com.dungzi.backend.domain.review.dto.ReviewResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthService authService;
    @Operation(summary = "후기 생성 api", description = "후기 생성을 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 후기 등록 완료")
            }
    )
    @PostMapping("/")
    public CommonResponse createReview(ReviewRequestDto.CreateReview requestDto, List<MultipartFile> files){
        User user = authService.getUserFromSecurity();
        UUID buildingId = reviewService.saveReview(requestDto,files,user);
        return CommonResponse.toResponse(CommonCode.OK, reviewService.saveReviewDetail(requestDto.toReviewDetailEntity(buildingId,user)));
    }

    @Operation(summary = "후기 삭제 api", description = "후기 삭제를 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 후기 삭제 완료")
            }
    )
    @DeleteMapping("/{reviewId}")
    public CommonResponse deleteReview(@PathVariable String reviewId){
        System.out.println(reviewId);
        reviewService.deleteReview(reviewId);
        return CommonResponse.toResponse(CommonCode.OK);
    }

    @Operation(summary = "후기 리스트 최신순 조회 api", description = "후기 상세정보 리스트 조회를 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 후기리스트 조회 완료")
            }
    )
    @GetMapping("/list")
    public CommonResponse getReviewList(Pageable pageable){
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("regDate").descending());
        List<ReviewDetailResponseDto> reviewList = reviewService.getReviewList(pageRequest);
        return CommonResponse.toResponse(CommonCode.OK,reviewList);
    }

    @Operation(summary = "해당 건물의 후기 상세정보 리스트 api", description = "해당 건물의 후기 상세정보 리스트 조회를 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 해당 건물의 후기리스트 조회 완료")
            }
    )
    @GetMapping("/")
    public CommonResponse getReview(@RequestParam("buildingId") String buildingId,Pageable pageable){
        List<ReviewDetailResponseDto> reviewDetailList = reviewService.getReview(buildingId, pageable);
        return CommonResponse.toResponse(CommonCode.OK,reviewDetailList);
    }

    @Operation(summary = "주소로 건물 검색 api", description = "해당 주소의 건물 리스트 조회를 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 해당 주소의 건물리스트 조회 완료")
            }
    )
    @GetMapping("/search")
    public CommonResponse findReview(@RequestParam("address") String address,Pageable pageable){
        List<ReviewResponseDto> reviewList = reviewService.findReview(address, pageable);
        return CommonResponse.toResponse(CommonCode.OK,reviewList);
    }


}
