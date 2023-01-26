package com.dungzi.backend.domain.chat.api;

import com.dungzi.backend.domain.chat.dao.ChatMessageDao;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.dto.ChatMessageRequestDto;
import com.dungzi.backend.domain.chat.dto.ChatMessageResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;


@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomDao chatRoomDao;
    private final AuthService authService;
    private final ChatMessageDao chatMessageDao;

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto messageSendDto) {
        //TODO dto json 메핑 오류 해결할것
        ChatRoom chatRoom = chatRoomDao.findById(messageSendDto.getChatRoomId()).get();
        User sender = authService.getUserFromSecurity();


        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .content(messageSendDto.getContent())
                .sender(sender)
                .build();
        chatMessageDao.save(chatMessage);

        ChatMessageResponseDto messageReceiveDto = ChatMessageResponseDto.builder()
                .chatRoomId(String.valueOf(chatMessage.getChatRoom().getChatRoomId()))
                .sendDate(chatMessage.getSendDate())
                .content(chatMessage.getContent())
                .sender(chatMessage.getSender().getNickname())
                .build();

        messagingTemplate.convertAndSend("/queue/" + messageSendDto.getChatRoomId().toString(), messageReceiveDto);
    }
}
