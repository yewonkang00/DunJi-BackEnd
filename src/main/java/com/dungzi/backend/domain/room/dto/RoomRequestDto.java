package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.domain.RoomInfo;
import com.dungzi.backend.domain.room.domain.RoomOption;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class RoomRequestDto {
    private String roomId;
    private UUID userId;
    private String univId;
    private String title;
    private String content;
    private int image;
    private double longtitude;
    private double latitude;
    private String address;
    private String addressDetail;
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


    public Room toRoomEntity(RoomDto roomDto) {

        UserDto userDto = null;
        userDto.setUserId(roomDto.getUserId().toString());
        User user = userDto.toEntity();

        return Room.builder()
                .roomId(UUID.fromString(roomDto.getRoomId()))
                .user(user)
//                .univId(requestDto.getUnivId())
                .title(roomDto.getTitle())
                .content(roomDto.getContent())
                .image(roomDto.getImage())
//                .regDate(requestDto.getRegDate)
                .delDate(roomDto.getDelDate())
                .dealDate(roomDto.getDealDate())
                .heartNum(roomDto.getHeartNum())
                .build();
    }

    public RoomAddress toAddressEntity(RoomRequestDto requestDto) {

        return RoomAddress.builder()
                .roomId(requestDto.getRoomId())
                .longtitude(requestDto.getLongtitude())
                .latitude(requestDto.getLatitude())
                .address(requestDto.getAddress())
                .addressDetail(requestDto.getAddressDetail())
                .build();
    }

    public RoomInfo toInfoEntity(RoomRequestDto requestDto) {

        return RoomInfo.builder()
                .roomId(requestDto.getRoomId())
                .startedAt(requestDto.getStartedAt())
                .finishedAt(requestDto.getFinishedAt())
                .tenancyAgreement(requestDto.isTenancyAgreement())
                .roomSize(requestDto.getRoomSize())
                .totalFloor(requestDto.getTotalFloor())
                .floor(requestDto.getFloor())
                .structure(requestDto.getStructure())
                .roomType(requestDto.getRoomType())
                .dealType(requestDto.getDealType())
                .deposit(requestDto.getDeposit())
                .price(requestDto.getPrice())
                .priceUnit(requestDto.getPriceUnit())
                .managementCost(requestDto.getManagementCost())
                .fullOption(requestDto.isFullOption())
                .build();
    }

    public RoomOption toOptionEntity(RoomOptionDto optionDto) {

        return RoomOption.builder()
                .roomId(optionDto.getRoomId())
                .options(optionDto.getOptions())
                .utility(optionDto.getUtility())
                .advantage(optionDto.getAdvantage())
                .build();
    }

}
