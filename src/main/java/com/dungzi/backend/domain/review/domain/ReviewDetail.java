package com.dungzi.backend.domain.review.domain;

import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewDetail extends BaseTimeEntity{

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID reviewDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private UUID buildingId; //fk로?

    private String content;

    private String image;

    private String address;

    private String addressDetail;

    private int totalRate;

    private int cleanRate;

    private int noiseRate;

    private int accessRate;

    private int hostRate;

    private  int facilityRate;

    private String period;

    private Date delDate;

}
