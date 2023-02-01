package com.dungzi.backend.domain.chat.dao;

import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageDao extends JpaRepository<ChatMessage, UUID> {
    Page<ChatMessage> findByChatRoomOrderBySendDateDesc(ChatRoom chatRoom, Pageable pageable);
}