package com.dungzi.backend.domain.chat.domain.dao;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserChatRoomDao extends JpaRepository<UserChatRoom, UUID> {
//    Optional<ChatRoom> findByUserChatRoomId(UUID userChatRoomId);


}