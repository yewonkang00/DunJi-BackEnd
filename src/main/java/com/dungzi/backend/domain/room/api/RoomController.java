package com.dungzi.backend.domain.room.api;

import com.dungzi.backend.domain.room.application.RoomService;
import com.dungzi.backend.domain.room.dto.*;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RoomController {
    private final AuthService authService;
    private final RoomService roomService;

    @PostMapping(value = "/rooms", produces = "application/json; charset=utf-8")
    public CommonResponse registerRoom(@RequestBody RoomRequestDto body, @RequestBody List<MultipartFile> files) throws Exception {

        log.info("[API] registerRoom");

        body.setUserId(authService.getUserFromSecurity().getUserId());
        String roomId = roomService.saveAction(body, files);

        return CommonResponse.toResponse(CommonCode.OK, "roomId : " + roomId);

    }


}
