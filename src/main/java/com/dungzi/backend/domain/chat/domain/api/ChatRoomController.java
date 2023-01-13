package com.dungzi.backend.domain.chat.domain.api;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.chat.domain.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.domain.dao.UserChatRoomDao;
import com.dungzi.backend.domain.chat.domain.dto.ChatRoomRequestDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final AuthService authService;
    private final ChatRoomDao chatRoomDao;
    private final UserChatRoomDao userChatRoomDao;
    private final UserDao userDao;

    @PostMapping("/room")
    public void createChatRoom(@RequestBody ChatRoomRequestDto.CreateChatRoom body) {
        //TODO userResponseDto 생성 몇 CommonResponse로 return!
        User ownUser = authService.getUserFromSecurity();
        User opponentUser = userDao.findByNickName(body.getUserNickName()).get();

        ChatRoom chatRoom = chatRoomDao.save(ChatRoom.builder()
                .chatRoomType(body.getChatRoomType())
                .opponentUser(opponentUser)
                .build());

        userChatRoomDao.save(UserChatRoom.builder()
                        .chatRoom(chatRoom)
                        .user(ownUser)
                        .build());
        userChatRoomDao.save(UserChatRoom.builder()
                .chatRoom(chatRoom)
                .user(opponentUser)
                .build());


    }

}
