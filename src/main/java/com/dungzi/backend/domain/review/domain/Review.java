package com.dungzi.backend.domain.review.domain;

import com.dungzi.backend.domain.review.dto.ReviewRequestDto;
import com.dungzi.backend.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String address;

    private float totalRate;

    private float cleanRate;

    private float noiseRate;

    private float accessRate;

    private float hostRate;

    private  float facilityRate;

    public void updateReview(ReviewRequestDto.CreateReview requestDto,int curCount) {
        this.totalRate = (this.totalRate*(curCount)+requestDto.getTotalRate())/(curCount+1);
        this.cleanRate = (this.cleanRate*(curCount)+requestDto.getCleanRate())/(curCount+1);
        this.noiseRate = (this.noiseRate*(curCount)+requestDto.getNoiseRate())/(curCount+1);
        this.accessRate = (this.accessRate*(curCount)+requestDto.getAccessRate())/(curCount+1);
        this.hostRate = (this.hostRate*(curCount)+requestDto.getHostRate())/(curCount+1);
        this.facilityRate = (this.facilityRate*(curCount)+requestDto.getFacilityRate())/(curCount+1);
    }

    public void deleteReview(ReviewDetail review,int curCount) {
        this.totalRate = (this.totalRate*(curCount)-review.getTotalRate())/(curCount-1);
        this.cleanRate = (this.cleanRate*(curCount)-review.getCleanRate())/(curCount-1);
        this.noiseRate = (this.noiseRate*(curCount)-review.getNoiseRate())/(curCount-1);
        this.accessRate = (this.accessRate*(curCount)-review.getAccessRate())/(curCount-1);
        this.hostRate = (this.hostRate*(curCount)-review.getHostRate())/(curCount-1);
        this.facilityRate = (this.facilityRate*(curCount)-review.getFacilityRate())/(curCount-1);
    }
}
