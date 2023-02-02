package com.dungzi.backend.domain.chat.dao;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRoomDao extends JpaRepository<ChatRoom, UUID> {
//    Optional<ChatRoom> findByChatRoomId(UUID chatRoomId);


}