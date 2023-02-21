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

    private int totalRate;

    private int cleanRate;

    private int noiseRate;

    private int accessRate;

    private int hostRate;

    private  int facilityRate;

    public void updateReview(ReviewRequestDto.CreateReview requestDto,int curCount) {
        this.totalRate = (this.totalRate*(curCount+1)+requestDto.getTotalRate())/(curCount+2);
        this.cleanRate = (this.cleanRate*(curCount+1)+requestDto.getCleanRate())/(curCount+2);
        this.noiseRate = (this.noiseRate*(curCount+1)+requestDto.getNoiseRate())/(curCount+2);
        this.accessRate = (this.accessRate*(curCount+1)+requestDto.getAccessRate())/(curCount+2);
        this.hostRate = (this.hostRate*(curCount+1)+requestDto.getHostRate())/(curCount+2);
        this.facilityRate = (this.facilityRate*(curCount+1)+requestDto.getFacilityRate())/(curCount+2);
    }
}
