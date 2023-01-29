package com.dungzi.backend.domain.chat.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.dungzi.backend.domain.chat.dao.ChatMessageDao;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.dao.UserChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.MessageType;
import com.dungzi.backend.domain.chat.dto.ChatMessageRequestDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatRoomDao chatRoomDao;
    @Mock
    private ChatMessageDao chatMessageDao;


    @InjectMocks
    private ChatMessageService chatMessageService;

    @Test
    @DisplayName("채팅메세지 저장 테스트-일반 메세지 타입")
    public void saveChatMessageTest() {
        //given
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomId(UUID.randomUUID())
                .build();
        ChatMessageRequestDto messageSendDto = ChatMessageRequestDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .content("Hello")
                .build();
        User sender = User.builder()
                .userId(UUID.randomUUID())
                .build();
        //when
        when(chatRoomDao.findById(messageSendDto.getChatRoomId())).thenReturn(Optional.of(chatRoom));
        ChatMessage savedChatMessage = ChatMessage.builder()
                .chatMessageId(UUID.randomUUID())
                .chatRoom(chatRoom)
                .content(messageSendDto.getContent())
                .messageType(MessageType.MESSAGE)
                .sender(sender)
                .build();
        when(chatMessageDao.save(any(ChatMessage.class))).thenReturn(savedChatMessage);
        ChatMessage returnedChatMessage = chatMessageService.saveChatMessage(messageSendDto, sender);
        //then
        assertThat(returnedChatMessage).isEqualTo(savedChatMessage);
    }

}