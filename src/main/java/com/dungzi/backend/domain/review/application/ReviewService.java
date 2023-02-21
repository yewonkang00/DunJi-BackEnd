package com.dungzi.backend.domain.review.application;

import com.dungzi.backend.domain.review.dao.ReviewDao;
import com.dungzi.backend.domain.review.dao.ReviewDetailDao;
import com.dungzi.backend.domain.review.domain.Review;
import com.dungzi.backend.domain.review.domain.ReviewDetail;
import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReviewService {

    private final ReviewDetailDao reviewDetailDao;
    private final ReviewDao reviewDao;

    public UUID saveReview(ReviewRequestDto.CreateReview requestDto){
        Optional<Review> find_review = reviewDao.findByAddress(requestDto.getAddress());

        if (find_review.isEmpty()) { //해당주소의 객체가 없다면
            return reviewDao.save(requestDto.toReviewEntity()).getReviewId();
            //새로운 객체 만들기
        }
        else{ //이미 해당주소의 객체가 있다면
            Review review = find_review.get();
            int curCount = reviewDetailDao.countByBuildingId(review.getReviewId());
            review.updateReview(requestDto,curCount);
            return review.getReviewId();
        }
    }
    public ReviewDetail saveReviewDetail(ReviewDetail reviewDetail){
        return reviewDetailDao.save(reviewDetail);
    }
}
