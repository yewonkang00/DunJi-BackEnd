package com.dungzi.backend.domain.review.dto;

import com.dungzi.backend.domain.review.domain.Review;
import com.dungzi.backend.domain.review.domain.ReviewDetail;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import java.util.UUID;

public class ReviewRequestDto {

    @Data
    public static class CreateReview{

        private String content;

        @NotBlank
        private String address;

        private String addressDetail;

        @NotBlank
        private int totalRate;

        @NotBlank
        private int cleanRate;

        @NotBlank
        private int noiseRate;

        @NotBlank
        private int accessRate;

        @NotBlank
        private int hostRate;

        @NotBlank
        private  int facilityRate;

        private String period;

        public ReviewDetail toReviewDetailEntity(UUID buildingId){
            return ReviewDetail.builder()
                    .buildingId(buildingId)
                    .content(this.content)
                    .address(this.address)
                    .addressDetail(this.addressDetail)
                    .totalRate(this.totalRate)
                    .cleanRate(this.cleanRate)
                    .noiseRate(this.noiseRate)
                    .accessRate(this.accessRate)
                    .hostRate(this.hostRate)
                    .facilityRate(this.facilityRate)
                    .period(this.period)
                    .build();

        }
        public Review toReviewEntity(){
            return Review.builder()
                    .address(this.address)
                    .totalRate(this.totalRate)
                    .cleanRate(this.cleanRate)
                    .noiseRate(this.noiseRate)
                    .accessRate(this.accessRate)
                    .hostRate(this.hostRate)
                    .facilityRate(this.facilityRate)
                    .build();

        }
    }
}
