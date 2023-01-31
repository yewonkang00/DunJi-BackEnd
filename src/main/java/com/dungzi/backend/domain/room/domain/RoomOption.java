package com.dungzi.backend.domain.room.domain;

import com.dungzi.backend.domain.room.application.SetAdvantageConverter;
import com.dungzi.backend.domain.room.application.SetOptionConverter;
import com.dungzi.backend.domain.room.dto.Advantage;
import com.dungzi.backend.domain.room.dto.Options;
import com.dungzi.backend.domain.room.dto.Utility;
import com.dungzi.backend.global.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.Enum;
import java.util.EnumSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RoomOption extends BaseTimeEntity {

    @Id
    @Column(name = "roomId", nullable = false)
    private String roomId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "roomId")
    private Room room;

    //@Convert(converter = SetOptionConverter.class)
    private EnumSet<Options> options;

    private EnumSet<Utility> utility;

    //@Convert(converter = SetAdvantageConverter.class)
    private EnumSet<Advantage> advantage;

}