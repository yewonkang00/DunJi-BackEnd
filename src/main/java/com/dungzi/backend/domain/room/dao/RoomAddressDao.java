package com.dungzi.backend.domain.room.dao;

import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.dto.RoomRequestDto;
import com.dungzi.backend.domain.room.dto.enumType.roomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomAddressDao extends JpaRepository<RoomAddress, UUID> {
    Optional<RoomAddress> findByRoomId(UUID roomId);

    @Modifying
    @Query("UPDATE RoomAddress a SET a.status = :status WHERE a.roomId = :roomId")
    void updateStatus(@Param("status") String status, @Param("roomId") UUID roomId);

    @Query("SELECT a FROM RoomAddress a JOIN FETCH a.roomInfo " +
            "WHERE a.longitude > :startLongitude and a.latitude > :startLatitude " +
            "and a.longitude < :endLongitude and a.latitude < :endLatitude " +
            "and a.status = 'active'")
    List<RoomAddress> findRoomByAddress(Double startLongitude, Double startLatitude, Double endLongitude, Double endLatitude);

}
