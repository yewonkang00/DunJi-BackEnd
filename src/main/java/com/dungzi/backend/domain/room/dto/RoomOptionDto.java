package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomInfo;
import com.dungzi.backend.domain.room.domain.RoomOption;
import lombok.Builder;
import lombok.Data;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class RoomOptionDto {

    private UUID roomId;
    // 옵션 추가
    private String options;
    private String utility;
//    private String advantage;
    private String startedAt;
    private String finishedAt;
    private boolean tenancyAgreement;

    public static RoomOptionDto toDto(RoomOption room) {

        return RoomOptionDto.builder()
                .utility(room.getUtility())
                .startedAt(room.getStartedAt())
                .finishedAt(room.getFinishedAt())
                .tenancyAgreement(room.isTenancyAgreement())
                .build();
    }
}