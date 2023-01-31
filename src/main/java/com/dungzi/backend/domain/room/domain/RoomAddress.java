package com.dungzi.backend.domain.room.domain;

import com.dungzi.backend.domain.room.dto.RoomAddressDto;
import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RoomAddress extends BaseTimeEntity {
    @Id
    @Column(name = "roomId", nullable = false)
    private String roomId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "roomId")
    private Room room;

//    @OneToOne
//    @JoinColumn(name = "univId")
//    private Univ univ;

    private double longtitude;
    private double latitude;
    private String address;
    private String addressDetail;

    public RoomAddressDto toRoomAddressDto() {

        return RoomAddressDto.builder()
                .roomId(this.getRoomId().toString())
                .longtitude(this.getLongtitude())
                .latitude(this.getLatitude())
                .address(this.getAddress())
                .address(this.getAddressDetail())
                .build();
    }
}