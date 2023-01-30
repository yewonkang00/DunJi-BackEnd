package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.RoomInfo;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class RoomInfoDto {

    private String roomId;
    private Date startedAt;
    private Date finishedAt;
    private boolean tenancyAgreement;
    private float roomSize;
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

    public RoomInfo toEntity(String roomId) {

        return RoomInfo.builder()
                .roomId(roomId)
                .startedAt(startedAt)
                .finishedAt(finishedAt)
                .tenancyAgreement(tenancyAgreement)
                .roomSize(roomSize)
                .totalFloor(totalFloor)
                .floor(floor)
                .structure(structure)
                .roomType(roomType)
                .dealType(dealType)
                .deposit(deposit)
                .price(price)
                .priceUnit(priceUnit)
                .managementCost(managementCost)
                .fullOption(fullOption)
                .build();
    }
}
