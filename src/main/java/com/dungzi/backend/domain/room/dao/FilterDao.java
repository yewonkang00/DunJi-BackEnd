package com.dungzi.backend.domain.room.dao;

import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.dto.RoomRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FilterDao {

    List<RoomAddress> findRoomByFilter(RoomRequestDto.FilterDto request);
}
