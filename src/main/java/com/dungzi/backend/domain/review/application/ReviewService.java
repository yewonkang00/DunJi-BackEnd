package com.dungzi.backend.domain.review.application;

import com.dungzi.backend.domain.review.dao.ReviewDao;
import com.dungzi.backend.domain.review.dao.ReviewDetailDao;
import com.dungzi.backend.domain.review.domain.Review;
import com.dungzi.backend.domain.review.domain.ReviewDetail;
import com.dungzi.backend.domain.review.dto.ReviewDetailResponseDto;
import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
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
    private final FileUploadService fileUploadService;


    public UUID saveReview(ReviewRequestDto.CreateReview requestDto, List<MultipartFile> files,User user){

        Optional<Review> find_review = reviewDao.findByAddress(requestDto.getAddress());


        if (find_review.isEmpty()) { //해당주소의 객체가 없다면
            UUID reviewId = reviewDao.save(requestDto.toReviewEntity()).getReviewId();
            fileUploadService.uploadRoomFile(reviewId.toString(), files);

            return reviewId;

            //새로운 객체 만들기
        }
        else{ //이미 해당주소의 객체가 있다면
            Review review = find_review.get();
            int curCount = reviewDetailDao.countByBuildingId(review.getReviewId());
            review.updateReview(requestDto,curCount);

            UUID reviewId = review.getReviewId();
            fileUploadService.uploadRoomFile(reviewId.toString(), files);

            return reviewId;
        }
    }
    public ReviewDetail saveReviewDetail(ReviewDetail reviewDetail){
        return reviewDetailDao.save(reviewDetail);
    }

    public void deleteReview(String reviewId){
        reviewDetailDao.deleteById(UUID.fromString(reviewId));
    }

    public List<ReviewDetailResponseDto> getReviewList(Pageable pageable){
        return changeToReviewResponseDto(reviewDetailDao.findAll(pageable));
    }

    public List<ReviewDetailResponseDto> getReview(String buildingId,Pageable pageable){
        return changeToReviewResponseDto(reviewDetailDao.findByBuildingId(UUID.fromString(buildingId),pageable));
    }

    private List<ReviewDetailResponseDto> changeToReviewResponseDto(Page<ReviewDetail> reviewList) {
        List<ReviewDetailResponseDto> response = new ArrayList<>();
        response.addAll(reviewList.stream()
                .map(reviewDetail -> ReviewDetailResponseDto.builder()
                        .userNickname(reviewDetail.getUser().getNickname())
                        .content(reviewDetail.getContent())
                        .address(reviewDetail.getAddress())
                        .addressDetail(reviewDetail.getAddressDetail())
                        .period(reviewDetail.getPeriod())
                        .regDate(ReviewDetailResponseDto.zonedDateTimeToDateTime(reviewDetail.getRegDate()))
                        .build())
                .collect(Collectors.toList()));
        return response;
    }

}
