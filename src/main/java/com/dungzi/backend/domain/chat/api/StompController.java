package com.dungzi.backend.domain.chat.api;

import com.dungzi.backend.domain.chat.application.ChatMessageService;
import com.dungzi.backend.domain.chat.application.StompService;
import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatMessageType;
import com.dungzi.backend.domain.chat.dto.ChatMessageRequestDto;
import com.dungzi.backend.domain.chat.dto.ChatMessageResponseDto;
import com.dungzi.backend.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@RequiredArgsConstructor
@Controller
public class StompController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final StompService stompService;

    @GetMapping("/test")
    public String test() {
        return "websocket.html";
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto messageSendDto, Message message) {
        //TODO 추후 해당 토큰으로 다시한번 인증을 해야하는지 혹은 simpUser를 통해 바로 가져오는것도 괜찮은지 생각
//        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
//        System.out.println("sessionAttributes = " + sessionAttributes.get("x-access-token"));
//        System.out.println("sessionAttributes = " + sessionAttributes.get("x-access-token"));
        User sender = stompService.getUserFromHeader(message);

        ChatMessage chatMessage = chatMessageService.saveChatMessage(messageSendDto, sender);

        ChatMessageResponseDto messageReceiveDto = ChatMessageResponseDto.builder()
                .sendDate(ChatMessageResponseDto.changeDateFormat(chatMessage.getSendDate()))
                .content(chatMessage.getContent())
                .sender(chatMessage.getSender().getNickname())
                .messageType(ChatMessageType.MESSAGE.getType())
                .build();

        messagingTemplate.convertAndSend("/queue/" + messageSendDto.getChatRoomId().toString(), messageReceiveDto);
    }


}
