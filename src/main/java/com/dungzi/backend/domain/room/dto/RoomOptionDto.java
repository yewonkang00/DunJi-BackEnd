package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.Room;
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
    private EnumSet<Options> options;
    private EnumSet<Utility> utility;
    private EnumSet<Advantage> advantage;

    public RoomOption toEntity(RoomOptionDto optionDto) {

        return RoomOption.builder()
                .roomId(roomId)
                .options(options)
                .utility(utility)
                .advantage(advantage)
                .build();
    }

    public RoomOptionDto toEnumDto(UUID roomId, EnumSet<Options> option, EnumSet<Utility> utility, EnumSet<Advantage> advantage) {
        return RoomOptionDto.builder()
                .roomId(roomId)
                .options(option)
                .utility(utility)
                .advantage(advantage)
                .build();
    }
}