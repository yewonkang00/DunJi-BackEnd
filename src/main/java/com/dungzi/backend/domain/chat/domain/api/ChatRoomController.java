package com.dungzi.backend.domain.chat.domain.api;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.chat.domain.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.domain.dao.UserChatRoomDao;
import com.dungzi.backend.domain.chat.domain.dto.ChatRoomRequestDto;
import com.dungzi.backend.domain.chat.domain.dto.ChatRoomResponseDto;
import com.dungzi.backend.domain.chat.domain.dto.ChatRoomResponseDto.CreateChatRoom;
import com.dungzi.backend.domain.chat.domain.dto.ChatRoomResponseDto.CreateChatRoom.CreateChatRoomBuilder;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.domain.user.dto.UserRequestDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "채팅방 생성 api", description = "채팅방 생성을 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "이전에 만든방이 존재하였기에 이전의 방 조회"),
                    @ApiResponse(responseCode = "201", description = "새로운 방 생성"),
            }
    )
    @PostMapping("/room")
    public CommonResponse createChatRoom(@RequestBody ChatRoomRequestDto.CreateChatRoom body) {
        //TODO userResponseDto 생성 몇 CommonResponse로 return!
        User ownUser = authService.getUserFromSecurity();
        User opponentUser = userDao.findByNickname(body.getUserNickName()).get();

        ChatRoom chatRoom = chatRoomDao.save(ChatRoom.builder()
                .chatRoomType(body.getChatRoomType())
                .build());

        userChatRoomDao.save(UserChatRoom.builder()
                        .chatRoom(chatRoom)
                        .user(ownUser)
                        .build());
        userChatRoomDao.save(UserChatRoom.builder()
                .chatRoom(chatRoom)
                .user(opponentUser)
                .build());

        CreateChatRoom response = CreateChatRoom.builder()
                .chatRoomId(chatRoom.getChatRoomId()).build();
        return CommonResponse.toResponse(CommonCode.OK, response);




    }

}
