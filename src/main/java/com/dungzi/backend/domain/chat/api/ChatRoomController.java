package com.dungzi.backend.domain.chat.api;

import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.dao.UserChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.application.ChatRoomService;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.chat.dto.ChatRoomRequestDto;
import com.dungzi.backend.domain.chat.dto.ChatRoomResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final AuthService authService;
    private final UserDao userDao;
    private final ChatRoomService chatRoomService;

    private final UserChatRoomDao userChatRoomDao;
    private final ChatRoomDao chatRoomDao;


    @Operation(summary = "채팅방 생성 api", description = "채팅방 생성을 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "이전에 만든방이 존재하였기에 이전의 방 조회"),
                    @ApiResponse(responseCode = "201", description = "새로운 방 생성"),
            }
    )
    @PostMapping("/room")
    public CommonResponse createChatRoom(@RequestBody ChatRoomRequestDto.CreateChatRoom body) {
        String opponentNickName=body.getUserNickName();

        //기존의 채팅방이 있는경우
        Optional<ChatRoom> existChatRoom = chatRoomService.findExistRoom(opponentNickName);
        if (existChatRoom.isPresent()) {
            return CommonResponse.toResponse(
                    CommonCode.OK,
                    ChatRoomResponseDto.CreateChatRoom.builder()
                            .chatRoomId(existChatRoom.get().getChatRoomId()).build());
        }

        ChatRoom createdRoom = chatRoomService.createChatRoom(opponentNickName);
        return CommonResponse.toResponse(
                CommonCode.CREATED,
                ChatRoomResponseDto.CreateChatRoom.builder()
                        .chatRoomId(createdRoom.getChatRoomId()).build());
    }

    @DeleteMapping("/room/{chatRoomId}")
    public CommonResponse createChatRoom(@PathVariable String chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return CommonResponse.toResponse(CommonCode.OK);
    }


}
