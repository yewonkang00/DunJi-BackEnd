package com.dungzi.backend.domain.chat.application;

import com.dungzi.backend.domain.chat.dao.ChatMessageDao;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatMessageType;
import com.dungzi.backend.domain.chat.dto.ChatMessageRequestDto;
import com.dungzi.backend.domain.chat.dto.ChatMessageResponseDto;
import com.dungzi.backend.domain.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatRoomDao chatRoomDao;
    private final ChatMessageDao chatMessageDao;

    @Transactional
    public ChatMessage saveChatMessage(ChatMessageRequestDto messageSendDto, User sender) {
        ChatRoom chatRoom = chatRoomDao.findById(messageSendDto.getChatRoomId()).get();

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .content(messageSendDto.getContent())
                .chatMessageType(ChatMessageType.MESSAGE)
                .sender(sender)
                .build();
        log.info("채팅 메세지:{}, 보낸 유저:{}, 채팅방:{}", messageSendDto.getContent(), sender.getNickname(),
                chatRoom.getChatRoomId());
        ChatMessage savedChatMessage = chatMessageDao.save(chatMessage);
        return savedChatMessage;
    }

    public List<ChatMessageResponseDto> findAllChatMessage(String roomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomDao.findById(UUID.fromString(roomId)).get();
        Page<ChatMessage> byChatRoomOrderBySendDateAsc = chatMessageDao.findByChatRoomOrderBySendDateDesc(chatRoom,
                pageable);
        return changeToResponseDto(byChatRoomOrderBySendDateAsc);
    }

    private List<ChatMessageResponseDto> changeToResponseDto(Page<ChatMessage> byChatRoomOrderBySendDateAsc) {
        List<ChatMessageResponseDto> response = new ArrayList<>();
        response.addAll(byChatRoomOrderBySendDateAsc.stream()
                .map(chatMessage -> ChatMessageResponseDto.builder()
                        .sender(chatMessage.getSender().getNickname())
                        .content(chatMessage.getContent())
                        .messageType(chatMessage.getChatMessageType().getType())
                        .sendDate(ChatMessageResponseDto.changeDateFormat(chatMessage.getSendDate()))
                        .build())
                .collect(Collectors.toList()));
        return response;
    }

}
