package com.dungzi.backend.domain.room.dao;

import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.dto.RoomRequestDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterDao {

    List<RoomAddress> findRoomByFilter(RoomRequestDto.FilterDto request);
}
