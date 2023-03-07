package com.dungzi.backend.domain.room.api;

import com.dungzi.backend.domain.room.application.RoomService;
import com.dungzi.backend.domain.room.dao.RoomDao;
import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.dto.*;
import com.dungzi.backend.domain.room.dto.RoomResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    // 매물 등록
    @Operation(summary = "매물 등록 api", description = "매물 등록을 위한 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "새로운 매물 등록")
            }
    )
    @PostMapping
    public ResponseEntity<CommonResponse> registerRoom(RoomRequestDto.RegisterDto body, List<MultipartFile> files) throws Exception {

        log.info("[API] Register Room");

        User user = authService.getUserFromSecurity();

        RoomResponseDto.RoomRegist response  = roomService.saveAction(body, files, user);

        log.info("[API] Room ID:{}, Registrant:{}", response.getRoomId(), user.getUserId());

        CommonResponse commonResponse = CommonResponse.toResponse(CommonCode.OK, response);
//        return CommonResponse.toResponse(CommonCode.OK, response);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    // 매물 상세 정보
    @Operation(summary = "매물 상세 정보 api", description = "매물 ID에 해당하는 상세 정보 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "상세 정보")
            }
    )
    @GetMapping
    public ResponseEntity<CommonResponse> roomDetail(@RequestParam("roomId") String roomId) {

        log.info("[API] Room Detail");
        log.info("매물 ID:{}", roomId);
//        Room room = roomService.findRoomDetail(UUID.fromString(roomId));
//        String response = roomService.findRoomDetail(UUID.fromString(roomId)).toString();
        RoomResponseDto.RoomDetail response = roomService.findRoomDetail(UUID.fromString(roomId));

        log.info("Room : {}", response.getRoomAddress().getAddressDetail());
//        log.info("Room userId : {}", response.getUserId());
        //RoomResponseDto.RoomDetail response = roomService.findRooms();
        CommonResponse commonResponse = CommonResponse.toResponse(CommonCode.OK, response);

//        return CommonResponse.toResponse(CommonCode.OK, response);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    // 매물 검색 - 좌표만 이용
    @Operation(summary = "좌표 검색 api", description = "좌표 범위 내에 해당하는 매물 리스트 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "좌표 검색")
            }
    )
    @GetMapping("/map")
    public ResponseEntity<CommonResponse> findRoomByAddress(
            @RequestParam("startLongitude") Double startLongitude,
            @RequestParam("startLatitude") Double startLatitude,
            @RequestParam("endLongitude") Double endLongitude,
            @RequestParam("endLatitude") Double endLatitude) {

        log.info("[API] findRoomByAddress");
        List<RoomResponseDto.RoomList> response = roomService.findRoomByAddress(startLongitude, startLatitude, endLongitude, endLatitude);

        log.info("Room : {}", response);
//        log.info("Room userId : {}", response.getUserId());
        //RoomResponseDto.RoomDetail response = roomService.findRooms();
//        return CommonResponse.toResponse(CommonCode.OK, response);
        CommonResponse commonResponse = CommonResponse.toResponse(CommonCode.OK, response);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    // 매물 삭제
    @Operation(summary = "매물 삭제 api", description = "매물 ID에 해당하는 매물 삭제 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "매물 삭제")
            }
    )
    @PatchMapping
    public ResponseEntity<CommonResponse> roomDelete(@RequestParam("roomId") String id) {
        log.info("[API] Room Delete");
        UUID roomId = UUID.fromString(id);

        return new ResponseEntity<>(roomService.deleteRoom(authService.getUserFromSecurity().getUserId(), roomId), HttpStatus.OK);
    }

    // 매물 신고 접수


    // 매물 신고 처리
//    @PatchMapping
//    public CommonResponse roomReport(@RequestParam("roomId") String id) {
//        log.info("[API] Room Report");
//        return roomService.reportRoom(UUID.fromString(id));
//    }

}