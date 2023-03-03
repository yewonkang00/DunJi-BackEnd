package com.dungzi.backend.domain.review.application;

import com.dungzi.backend.domain.review.dao.ReviewDao;
import com.dungzi.backend.domain.review.dao.ReviewDetailDao;
import com.dungzi.backend.domain.review.dao.ReviewReportDao;
import com.dungzi.backend.domain.review.domain.Review;
import com.dungzi.backend.domain.review.domain.ReviewDetail;
import com.dungzi.backend.domain.review.dto.ReviewDetailResponseDto;
import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
import com.dungzi.backend.domain.review.dto.ReviewResponseDto;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewService {

    private final ReviewDetailDao reviewDetailDao;
    private final ReviewDao reviewDao;

    private final ReviewReportDao reviewReportDao;
    private final FileUploadService fileUploadService;


    public UUID saveReview(ReviewRequestDto.CreateReview requestDto, List<MultipartFile> files,User user){

        Optional<Review> findReview = reviewDao.findByAddress(requestDto.getAddress());


        if (findReview.isEmpty()) { //해당주소의 객체가 없다면
            UUID reviewId = reviewDao.save(requestDto.toReviewEntity(user)).getReviewId();
            fileUploadService.uploadReviewFile(reviewId.toString(), files);

            return reviewId;

            //새로운 객체 만들기
        }
        else{ //이미 해당주소의 객체가 있다면
            Review review = findReview.get();
            int curCount = reviewDetailDao.countByBuildingId(review.getReviewId());
            review.updateReview(requestDto,curCount);

            UUID reviewId = review.getReviewId();
            fileUploadService.uploadReviewFile(reviewId.toString(), files);

            return reviewId;
        }
    }
    public ReviewDetail saveReviewDetail(ReviewDetail reviewDetail){
        return reviewDetailDao.save(reviewDetail);
    }

    public void deleteReview(String reviewDetailId){
        ReviewDetail reviewDetail = reviewDetailDao.findById(UUID.fromString(reviewDetailId)).get();
        Review review = reviewDao.findById(reviewDetail.getBuildingId()).get();
        int curCount = reviewDetailDao.countByBuildingId(reviewDetail.getBuildingId());

        review.deleteReview(reviewDetail,curCount);
        reviewDetailDao.deleteById(UUID.fromString(reviewDetailId));
    }

    public List<ReviewDetailResponseDto> getReviewList(Pageable pageable){
        return changeToReviewDetailResponseDto(reviewDetailDao.findAll(pageable));
    }

    public List<ReviewDetailResponseDto> getReview(String buildingId,Pageable pageable){
        return changeToReviewDetailResponseDto(reviewDetailDao.findByBuildingId(UUID.fromString(buildingId),pageable));
    }

    public List<ReviewResponseDto> findReview(String address,Pageable pageable){
        return changeToReviewResponseDto(reviewDao.findByAddress(address,pageable));
    }

    public void reportReview(ReviewRequestDto.ReportReview requestDto,User user){
        reviewReportDao.save(requestDto.toReportEntity(requestDto.getReviewId(),requestDto.getReportType(),user.getUserId()));
    }

    private List<ReviewResponseDto> changeToReviewResponseDto(Page<Review> reviewList) {
        List<ReviewResponseDto> response = new ArrayList<>();
        long count = reviewList.stream().count();
        response.addAll(reviewList.stream()
                .map(review -> ReviewResponseDto.builder()
                        .address(review.getAddress())
                        .totalRate(review.getTotalRate())
                        .count(count)
                        .build())
                .collect(Collectors.toList()));
        return response;
    }

    private List<ReviewDetailResponseDto> changeToReviewDetailResponseDto(Page<ReviewDetail> reviewList) {
        List<ReviewDetailResponseDto> response = new ArrayList<>();
        response.addAll(reviewList.stream()
                .map(reviewDetail -> ReviewDetailResponseDto.builder()
                        .userNickname(reviewDetail.getUser().getNickname())
                        .content(reviewDetail.getContent())
                        .address(reviewDetail.getAddress())
                        .addressDetail(reviewDetail.getAddressDetail())
                        .period(reviewDetail.getPeriod())
                        .totalRate(reviewDetail.getTotalRate())
                        .regDate(ReviewDetailResponseDto.zonedDateTimeToDateTime(reviewDetail.getRegDate()))
                        .build())
                .collect(Collectors.toList()));
        return response;
    }

}
