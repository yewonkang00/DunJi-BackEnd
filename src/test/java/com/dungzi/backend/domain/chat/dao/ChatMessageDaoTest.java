package com.dungzi.backend.domain.chat.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatMessageType;
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
                .chatRoom(chatRoom).content("test message 1").chatMessageType(ChatMessageType.MESSAGE)
                .sendDate(LocalDateTime.now()).build();
        chatMessageDao.save(chatMessage1);
        chatMessageDao.flush();
        ChatMessage chatMessage2 = ChatMessage.builder()
                .chatRoom(chatRoom).content("test message 2").chatMessageType(ChatMessageType.MESSAGE)
                .sendDate(LocalDateTime.now().plusHours(1)).build();
        chatMessageDao.save(chatMessage2);
        chatMessageDao.flush();
        ChatMessage chatMessage3 = ChatMessage.builder()
                .chatRoom(chatRoom).content("test message 3").chatMessageType(ChatMessageType.MESSAGE)
                .sendDate(LocalDateTime.now().plusDays(1)).build();
        chatMessageDao.save(chatMessage3);
        chatMessageDao.flush();

        // When
        Page<ChatMessage> chatMessages = chatMessageDao.findByChatRoomOrderBySendDateDesc(chatRoom, PageRequest.of(0, 10));
        // Then
        assertThat(chatMessages.getContent().get(0)).isEqualTo(chatMessage3);
        assertThat(chatMessages.getContent().get(1)).isEqualTo(chatMessage2);
        assertThat(chatMessages.getContent().get(2)).isEqualTo(chatMessage1);
    }
}
