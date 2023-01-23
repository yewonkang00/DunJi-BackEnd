package com.dungzi.backend.domain.chat.dao;

import com.dungzi.backend.domain.chat.domain.ChatMessage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageDao extends JpaRepository<ChatMessage, UUID> {
}