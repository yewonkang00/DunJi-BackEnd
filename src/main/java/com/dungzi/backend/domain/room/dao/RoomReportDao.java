package com.dungzi.backend.domain.room.dao;

import com.dungzi.backend.domain.room.domain.RoomReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoomReportDao extends JpaRepository<RoomReport, UUID> {

}
