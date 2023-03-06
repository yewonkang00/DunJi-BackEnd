package com.dungzi.backend.domain.room.domain;

import com.dungzi.backend.domain.room.dto.RoomAddressDto;
import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RoomAddress extends BaseTimeEntity {
//    @Id
//    @Column(name = "room_id", nullable = false,length = 36)
//    @Type(type = "uuid-char")
//    private UUID roomId;

    @Id
    @Column(name = "room_id", nullable = false,length = 36)
    @Type(type = "uuid-char")
    private UUID roomId;

//    @OneToOne(fetch = FetchType.LAZY)
//    //@MapsId
//    @JoinColumn(name = "roomId")
//    private Room room;

    @OneToOne(mappedBy = "roomAddress")
    private Room room;

    @OneToOne
    @JoinColumn(name = "roomId")
    private RoomInfo roomInfo;

//    @OneToOne
//    @JoinColumn(name = "univId")
//    private Univ univ;

    private double longitude;
    private double latitude;
    private String address;
    private String addressDetail;
    private String sigungu;
    private String dong;
    private String status;
}
