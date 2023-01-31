package com.dungzi.backend.domain.chat.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.MessageType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class ChatMessageDaoTest {

    @Autowired
    private ChatMessageDao chatMessageDao;

    @Autowired
    private ChatRoomDao chatRoomDao;

    @Test
    @DisplayName("채팅 메세지 날짜 최신날짜 우선 정렬 조회")
    public void findByChatRoomOrderBySendDateDescTest() {
        // Given
        ChatRoom chatRoom = new ChatRoom();
        chatRoomDao.save(chatRoom);

        ChatMessage chatMessage1 = ChatMessage.builder()
                .chatRoom(chatRoom).content("test message 1").messageType(MessageType.MESSAGE)
                .sendDate(LocalDateTime.now()).build();
        ChatMessage chatMessage2 = ChatMessage.builder()
                .chatRoom(chatRoom).content("test message 2").messageType(MessageType.MESSAGE)
                .sendDate(LocalDateTime.now().minusHours(1)).build();
        ChatMessage chatMessage3 = ChatMessage.builder()
                .chatRoom(chatRoom).content("test message 3").messageType(MessageType.MESSAGE)
                .sendDate(LocalDateTime.now().minusDays(1)).build();
        chatMessageDao.saveAll(List.of(chatMessage1, chatMessage2, chatMessage3));
        chatMessageDao.flush();
        // When
        Page<ChatMessage> chatMessages = chatMessageDao.findByChatRoomOrderBySendDateAsc(chatRoom, PageRequest.of(0, 10));
        // Then
        assertThat(chatMessages.getContent().get(0)).isEqualTo(chatMessage1);
        assertThat(chatMessages.getContent().get(1)).isEqualTo(chatMessage2);
        assertThat(chatMessages.getContent().get(2)).isEqualTo(chatMessage3);
    }
}
