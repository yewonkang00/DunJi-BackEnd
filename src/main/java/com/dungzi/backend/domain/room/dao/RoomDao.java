package com.dungzi.backend.domain.room.dao;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.dto.RoomRequestDto;
import com.dungzi.backend.domain.room.dto.RoomResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomDao extends JpaRepository<Room, UUID> {
    Optional<Room> findByRoomId(UUID roomId);

    @Query("SELECT r FROM Room r "+
            "JOIN FETCH r.roomAddress "+
            "JOIN FETCH r.roomOption "+
            "JOIN FETCH r.roomInfo "+
            "WHERE r.roomId = :roomId")
    Room findRoomDetailByRoomId(UUID roomId);
}
