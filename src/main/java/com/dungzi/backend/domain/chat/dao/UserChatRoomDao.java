package com.dungzi.backend.domain.chat.dao;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserChatRoomDao extends JpaRepository<UserChatRoom, UUID> {
    List<UserChatRoom> findByChatRoom(ChatRoom chatRoom);
    List<UserChatRoom> findByUser(User user);

}