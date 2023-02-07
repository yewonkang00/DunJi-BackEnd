package com.dungzi.backend.domain.room.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.UUID;

public class RoomResponseDto {

    @Builder
    @Data
    public static class RoomRegist {
        private UUID roomId;
    }

    @Builder
    @Data
    public static class RoomDetail {
        //private String roomId;

    }

    @Builder
    @Data
    public static class RoomList {
        private String roomId;
        private double longtitude;
        private double latitude;
        private String address;
        private int price;
        private String priceUnit;
        private int floor;
        private double roomSize;
        private int deposit;
        private String roomType;
        private String dealType;
    }
}
