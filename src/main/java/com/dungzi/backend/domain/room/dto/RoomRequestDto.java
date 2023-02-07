package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.domain.RoomInfo;
import com.dungzi.backend.domain.room.domain.RoomOption;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

public class RoomRequestDto {

    @Data
    @Builder
    public static class RegisterDto {
//        private RoomDto room;
//        private RoomAddressDto address;
//        private RoomInfoDto info;
//        private RoomRequestOptionDto option;

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
        private boolean electricity;
        private boolean gas;
        private boolean water;
        private boolean internet;
        private boolean tv;
    }

    @Data
    @Builder
    public static class RoomRequestRoomDto {
        private String roomId;
        private String userId;
        private String univId;
        private String title;
        private String content;
        private int image;
    }

    @Data
    @Builder
    public static class RoomRequestAddressDto {
        private double longtitude;
        private double latitude;
        private String address;
        private String addressDetail;
        private String sigungu;
        private String dong;
    }

    @Data
    @Builder
    public static class RoomRequestInfoDto {
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

    @Data
    @Builder
    public static class RoomRequestOptionDto {
        private UUID roomId;
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
        private boolean electricity;
        private boolean gas;
        private boolean water;
        private boolean internet;
        private boolean tv;
    }

//    public Room toRoomEntity(RoomDto roomDto) {
//
//        UserDto userDto = null;
//        userDto.setUserId(roomDto.getUserId().toString());
//        User user = userDto.toEntity();
//
//        return Room.builder()
//                .roomId(UUID.fromString(roomDto.getRoomId()))
//                .user(user)
////                .univId(requestDto.getUnivId())
//                .title(roomDto.getTitle())
//                .content(roomDto.getContent())
//                .image(roomDto.getImage())
////                .regDate(requestDto.getRegDate)
//                .delDate(roomDto.getDelDate())
//                .dealDate(roomDto.getDealDate())
//                .heartNum(roomDto.getHeartNum())
//                .build();
//    }
//
//    public RoomAddress toAddressEntity(RegisterDto requestDto) {
//
//        return RoomAddress.builder()
//                .roomId(requestDto.getRoom().getRoomId())
//                .longtitude(requestDto.getAddress().getLongtitude())
//                .latitude(requestDto.getAddress().getLatitude())
//                .address(requestDto.getAddress().getAddress())
//                .addressDetail(requestDto.getAddress().getAddressDetail())
//                .build();
//    }
//
//    public RoomInfo toInfoEntity(RoomRequestInfoDto requestDto) {
//
//        return RoomInfo.builder()
//                .roomId(requestDto.getRoomId())
//                .startedAt(requestDto.getInfo().getStartedAt())
//                .finishedAt(requestDto.getInfo().getFinishedAt())
//                .tenancyAgreement(requestDto.getInfo().isTenancyAgreement())
//                .roomSize(requestDto.getInfo().getRoomSize())
//                .totalFloor(requestDto.getInfo().getTotalFloor())
//                .floor(requestDto.getInfo().getFloor())
//                .structure(requestDto.getInfo().getStructure())
//                .roomType(requestDto.getInfo().getRoomType())
//                .dealType(requestDto.getInfo().getDealType())
//                .deposit(requestDto.getInfo().getDeposit())
//                .price(requestDto.getInfo().getPrice())
//                .priceUnit(requestDto.getInfo().getPriceUnit())
//                .managementCost(requestDto.getInfo().getManagementCost())
//                .fullOption(requestDto.getInfo().isFullOption())
//                .build();
//    }

    public RoomOption toOptionEntity(RoomOptionDto optionDto) {

        return RoomOption.builder()
                .roomId(optionDto.getRoomId())
                .options(optionDto.getOptions())
                .utility(optionDto.getUtility())
                .advantage(optionDto.getAdvantage())
                .build();
    }
}
