package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomReport;
import com.dungzi.backend.domain.user.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoomReportDto {
    private UUID reportId;
    private User user;
    private Room room;
    private String report;


    public RoomReport toEntity(RoomReportDto reportDto) {
        return RoomReport.builder()
                .reportType(reportDto.getReport())
                .user(reportDto.getUser())
                .room(reportDto.room)
                .build();
    }
}

