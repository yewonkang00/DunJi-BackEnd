package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomInfo;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class RoomInfoDto {

    private UUID roomId;
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
    private boolean elevators;
    private boolean parking;
    private boolean pets;
    private boolean womenOnly;
    private boolean loan;

//    public RoomInfo toEntity(UUID roomId) {
//
//        return RoomInfo.builder()
//                .roomId(roomId)
////                .startedAt(startedAt)
////                .finishedAt(finishedAt)
////                .tenancyAgreement(tenancyAgreement)
//                .roomSize(roomSize)
//                .totalFloor(totalFloor)
//                .floor(floor)
//                .structure(structure)
//                .roomType(roomType)
//                .dealType(dealType)
//                .deposit(deposit)
//                .price(price)
//                .priceUnit(priceUnit)
//                .managementCost(managementCost)
//                .fullOption(fullOption)
//                .elevators(elevators)
//                .parking(parking)
//                .pets(pets)
//                .womenOnly(womenOnly)
//                .loan(loan)
//                .build();
//    }

    public static RoomInfoDto toDto(RoomInfo room) {

        return RoomInfoDto.builder()
                .roomSize(room.getRoomSize())
                .totalFloor(room.getTotalFloor())
                .floor(room.getFloor())
                .structure(room.getStructure())
                .roomType(room.getRoomType())
                .dealType(room.getDealType())
                .deposit(room.getDeposit())
                .price(room.getPrice())
                .priceUnit(room.getPriceUnit())
                .managementCost(room.getManagementCost())
                .fullOption(room.isFullOption())
                .elevators(room.isElevators())
                .parking(room.isParking())
                .pets(room.isPets())
                .womenOnly(room.isWomenOnly())
                .loan(room.isLoan())
                .build();
    }
}
