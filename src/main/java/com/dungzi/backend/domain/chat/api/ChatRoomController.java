package com.dungzi.backend.domain.chat.api;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.application.ChatRoomService;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.chat.dto.ChatRoomRequestDto;
import com.dungzi.backend.domain.chat.dto.ChatRoomResponseDto;
import com.dungzi.backend.domain.chat.dto.ChatRoomResponseDto.UsersChatRooms;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    @Operation(summary = "채팅방 생성 api", description = "채팅방 생성을 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "이전에 만든방이 존재하였기에 이전의 방 조회"),
                    @ApiResponse(responseCode = "201", description = "새로운 방 생성"),
            }
    )
    @PostMapping("/room")
    public CommonResponse createChatRoom(@RequestBody ChatRoomRequestDto.CreateChatRoom body) {
        String opponentNickName = body.getOpponentUserName();

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

    @Operation(summary = "채팅방 삭제 api", description = "채팅방 삭제 API"
            + "채팅방 삭제시 자신과 상대방의 채팅방에서 조회가 되지 않습니다")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상적으로 채팅방 삭제"),
            }
    )
    @DeleteMapping("/room/{chatRoomId}")
    public CommonResponse deleteChatRoom(@PathVariable String chatRoomId) {
        chatRoomService.deleteChatRoom(chatRoomId);
        return CommonResponse.toResponse(CommonCode.OK);
    }

    @Operation(summary = "채팅방 조회 api", description = "Type은 두가지만 가능합니다.\n"
            + "seek: 내놓은방거래\n"
            + "given_out:구하는방 거래")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상적인 채팅방 조회"),
            }
    )
    @GetMapping("/room")
    public CommonResponse selectChatRoom(@RequestParam("type") String seek) {
        List<ChatRoomResponseDto.UsersChatRooms> response = chatRoomService.findChatRoomsBySeekType(ChatRoomType.findByType(seek));
        return CommonResponse.toResponse(CommonCode.OK,response);
    }


}
