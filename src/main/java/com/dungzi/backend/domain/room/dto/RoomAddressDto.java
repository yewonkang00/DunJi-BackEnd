package com.dungzi.backend.domain.room.dto;

import com.dungzi.backend.domain.room.domain.RoomAddress;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomAddressDto {

    private String roomId;
    private double longtitude;
    private double latitude;
    private String address;
    private String addressDetail;

    public RoomAddress toEntity(String roomId) {

        return RoomAddress.builder()
                .roomId(roomId)
                .longtitude(longtitude)
                .latitude(latitude)
                .address(address)
                .address(addressDetail)
                .build();
    }
}
