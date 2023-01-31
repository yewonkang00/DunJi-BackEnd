package com.dungzi.backend.domain.room.domain;

import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RoomInfo extends BaseTimeEntity {

    @Id
    @Column(name = "roomId", nullable = false)
    private String roomId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "roomId")
    private Room room;

//    private status;

    private String startedAt;
    private String finishedAt;
    private boolean tenancyAgreement;
    private double roomSize;
    private int totalFloor;
    private int floor;
    private String structure;
    private String roomType;
    private String dealType;
    private int deposit;
    private int price;
    private String priceUnit;
    private int managementCost;
    private boolean fullOption;

}
