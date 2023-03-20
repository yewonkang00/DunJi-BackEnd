package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomAddress;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RoomResponseDto {

    @Builder
    @Data
    public static class RoomRegist {
        private UUID roomId;
    }

    /*
    @Builder
    @Data
    public static class RoomDetail {
        private UUID roomId;
        private UUID userId;
        private String univId;
        private String title;
        private String content;
        private int image;
        private double longitude;
        private double latitude;
        private String address;
        private String addressDetail;
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
        private boolean airConditioner;
        private boolean refrigerator;
        private boolean washingMachine;
        private boolean gasStove;
        private boolean induction;
        private boolean microwave;
        private boolean desk;
        private boolean bookcase;
        private boolean bed;
        private boolean closet;
        private boolean sink;
        private boolean shoeCabinet;
        private boolean elevators;
        private boolean parking;
        private boolean pets;
        private boolean womenOnly;
        private boolean loan;
        private String utility;

    }
    */

    @Builder
    @Data
    public static class RoomDetail {
        private String roomId;
//        private String userId;
        private String userName;
        private String regDate;

        private String title;

        private String content;
        private int heartNum;
        private RoomInfoDto roomInfo;
        private RoomAddressDto roomAddress;
        private List<String> option;
        private String utility;
        private String startedAt;
        private String finishedAt;
        private boolean tenancyAgreement;
        private List<String> roomImage;

    }

    @Builder
    @Data
    public static class RoomList {
        private String roomId;
        private double longitude;
        private double latitude;
        private String sigungu;
        private String dong;
        private String priceUnit;
        private int deposit;
        private int price;
        private String roomType;
        private int floor;
        private double roomSize;
        private String dealType;
    }

    public static RoomDetail toDto(Room room, String regDate, RoomInfoDto roomInfoDto, RoomAddressDto roomAddressDto,
                                   List<String> option, List<String> roomImage) {
        return RoomDetail.builder()
                .roomId(room.getRoomId().toString())
                .userName(room.getUser().getNickname())
                .regDate(regDate)
                .title(room.getTitle())
                .content(room.getContent())
                .heartNum(room.getHeartNum())
                .roomInfo(roomInfoDto)
                .roomAddress(roomAddressDto)
                .option(option)
                .utility(room.getRoomOption().getUtility())
                .startedAt(room.getRoomOption().getStartedAt())
                .finishedAt(room.getRoomOption().getFinishedAt())
                .tenancyAgreement(room.getRoomOption().isTenancyAgreement())
                .roomImage(roomImage)
                .build();
    }

    public static RoomList toDto(RoomAddress room) {
        return RoomList.builder()
                .roomId(room.getRoomId().toString())
                .longitude(room.getLongitude())
                .latitude(room.getLatitude())
                .sigungu(room.getSigungu())
                .dong(room.getDong())
                .priceUnit(room.getRoomInfo().getPriceUnit())
                .deposit(room.getRoomInfo().getDeposit())
                .price(room.getRoomInfo().getPrice())
                .roomType(room.getRoomInfo().getRoomType())
                .floor(room.getRoomInfo().getFloor())
                .roomSize(room.getRoomInfo().getRoomSize())
                .dealType(room.getRoomInfo().getDealType())
                .build();
    }
}
