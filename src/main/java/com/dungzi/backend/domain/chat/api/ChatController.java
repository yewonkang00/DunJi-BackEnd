package com.dungzi.backend.domain.chat.api;

import com.dungzi.backend.domain.chat.dao.ChatMessageDao;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatMessage;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.application.ChatRoomService;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.chat.dto.ChatMessageResponseDto;
import com.dungzi.backend.domain.chat.dto.ChatRoomRequestDto;
import com.dungzi.backend.domain.chat.dto.ChatRoomResponseDto;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageDao chatMessageDao;
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

    @Operation(summary = "채팅방 메세지 조회 api", description = "채팅방 메세지를 조회힙니다."
            + "\n paging을 사용하기에 pageable 요청에 {\n"
            + "  \"page\": 1,\n"
            + "  \"size\": 5\n"
            + "}"
            + "\n과 같은값을 넣어 테스트를 하면 됩니다."
            + "\npage:요청할 페이지 번호"
            + "\nsize : 한 페이지 당 조회 할 갯수"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "정상적인 채팅 메세지 조회"),
            }
    )
    @GetMapping("/message")
    public CommonResponse findMessage(@RequestParam("roomId") String roomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomDao.findById(UUID.fromString(roomId)).get();
        Page<ChatMessage> byChatRoomOrderBySendDateAsc = chatMessageDao.findByChatRoomOrderBySendDateDesc(chatRoom,
                pageable);
        List<ChatMessageResponseDto> response = new ArrayList<>();
        for (ChatMessage chatMessage : byChatRoomOrderBySendDateAsc) {
            response.add(ChatMessageResponseDto.builder()
                    .sender(chatMessage.getSender().getNickname())
                    .content(chatMessage.getContent())
                    .messageType(chatMessage.getChatMessageType().getType())
                    .sendDate(ChatMessageResponseDto.changeDateFormat(chatMessage.getSendDate())).build())
            ;

        }
        return CommonResponse.toResponse(CommonCode.OK,response);
    }



}
