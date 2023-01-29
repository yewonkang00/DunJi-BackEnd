package com.dungzi.backend.domain.chat.api;

import com.dungzi.backend.domain.chat.dao.ChatMessageDao;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.dto.ChatMessageRequestDto;
import com.dungzi.backend.domain.chat.dto.ChatMessageResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomDao chatRoomDao;
    private final AuthService authService;
    private final ChatMessageDao chatMessageDao;
    private final UserDao userDao;

    @GetMapping("/test")
    public String test() {
        return "websocket.html";
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto messageSendDto, Message message) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);


        //TODO 추후 해당 토큰으로 다시한번 인증을 해야하는지 혹은 simpUser를 통해 바로 가져오는것도 괜찮은지 생각
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        System.out.println("sessionAttributes = " + sessionAttributes.get("x-access-token"));
        System.out.println("sessionAttributes = " + sessionAttributes.get("x-access-token"));

        UsernamePasswordAuthenticationToken simpleToken = (UsernamePasswordAuthenticationToken) accessor.getHeader(
                "simpUser");
        User sender = (User) simpleToken.getPrincipal();

        ChatMessage savedChatMessage = getChatMessage(messageSendDto, sender);

        ChatMessageResponseDto messageReceiveDto = ChatMessageResponseDto.builder()
                .chatRoomId(String.valueOf(savedChatMessage.getChatRoom().getChatRoomId()))
                .sendDate(savedChatMessage.getSendDate())
                .content(savedChatMessage.getContent())
                .sender(savedChatMessage.getSender().getNickname())
                .build();

        messagingTemplate.convertAndSend("/queue/" + messageSendDto.getChatRoomId().toString(), messageReceiveDto);
    }

    @Transactional
    private ChatMessage getChatMessage(ChatMessageRequestDto messageSendDto, User sender) {
        ChatRoom chatRoom = chatRoomDao.findById(messageSendDto.getChatRoomId()).get();
        User user = userDao.findById(sender.getUserId()).get();

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .content(messageSendDto.getContent())
                .sender(user)
                .build();
        ChatMessage savedChatMessage = chatMessageDao.save(chatMessage);
        return savedChatMessage;
    }
}
