package com.dungzi.backend.domain.room.api;

import com.dungzi.backend.domain.room.application.RoomService;
import com.dungzi.backend.domain.room.dto.*;
import com.dungzi.backend.domain.room.domain.*;
import com.dungzi.backend.domain.room.dto.RoomResponseDto.RoomRegist;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RoomController {
    private final AuthService authService;
    private final RoomService roomService;

    @PostMapping(value = "/rooms", produces = "application/json;", consumes = "multipart/form-data")
    public CommonResponse registerRoom(RoomRequestDto.RegisterDto body, List<MultipartFile> files) throws Exception {

        log.info("[API] Register Room");

//        body.getRoom().setUserId(authService.getUserFromSecurity().getUserId().toString());
       // body.setUserId(authService.getUserFromSecurity().getUserId());

       // body.setUserId(authService.getUserFromSecurity().getUserId());
       // System.out.println("***"+authService.getUserFromSecurity().getUserId().toString());
        User user = authService.getUserFromSecurity();
        RoomRegist response  = roomService.saveAction(body, files, user);
        //Room room = body.getRoom().toEntity(body.getRoom());
        //RoomAddress address = body.getAddress().toEntity();


        log.info("매물 ID:{}, 등록한 유저:{}", response.getRoomId(), body.getUserId());

        return CommonResponse.toResponse(CommonCode.OK, response);

    }

    @GetMapping(value = "/rooms")
    public CommonResponse roomDetail(@RequestParam("roomId") String roomId) {

        log.info("[API] Room Detail");

        log.info("매물 ID:{}", roomId);
        return CommonResponse.toResponse(CommonCode.OK, "");
    }


}
