package com.dungzi.backend.domain.room.api;

import com.dungzi.backend.domain.room.application.RoomService;
import com.dungzi.backend.domain.room.dao.RoomDao;
import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.dto.*;
import com.dungzi.backend.domain.room.dto.RoomResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final AuthService authService;
    private final RoomService roomService;

    @PostMapping
    public CommonResponse registerRoom(RoomRequestDto.RegisterDto body, List<MultipartFile> files) throws Exception {

        log.info("[API] Register Room");

        User user = authService.getUserFromSecurity();

        RoomResponseDto.RoomRegist response  = roomService.saveAction(body, files, user);

        log.info("[API] Room ID:{}, Registrant:{}", response.getRoomId(), user.getUserId());

        return CommonResponse.toResponse(CommonCode.OK, response);

    }

    @GetMapping
    public CommonResponse roomDetail(@RequestParam("roomId") String roomId) {

        log.info("[API] Room Detail");
        log.info("매물 ID:{}", roomId);
        //RoomResponseDto.RoomDetail response = roomService.findRooms();
        return CommonResponse.toResponse(CommonCode.OK, "response");
    }

    @PatchMapping
    public CommonResponse roomDelete(@RequestParam("roomId") String id) {
        log.info("[API] Room Delete");
        User user = authService.getUserFromSecurity();
        UUID userId = user.getUserId();
        UUID roomId = UUID.fromString(id);

        return roomService.deleteRoom(userId, roomId);
    }

}