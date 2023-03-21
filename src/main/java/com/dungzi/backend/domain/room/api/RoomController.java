package com.dungzi.backend.domain.room.api;

import com.dungzi.backend.domain.room.application.RoomService;
import com.dungzi.backend.domain.room.dto.*;
import com.dungzi.backend.domain.room.dto.RoomResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.response.code.CommonCode;
import com.dungzi.backend.global.common.response.CommonResponse;
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
            @RequestParam("startLongitude") double startLongitude,
            @RequestParam("startLatitude") double startLatitude,
            @RequestParam("endLongitude") double endLongitude,
            @RequestParam("endLatitude") double endLatitude) {

        log.info("[API] findRoomByAddress");
        List<RoomResponseDto.RoomList> response = roomService.findRoomByAddress(startLongitude, startLatitude, endLongitude, endLatitude);

        log.info("Room : {}", response);
//        log.info("Room userId : {}", response.getUserId());
        //RoomResponseDto.RoomDetail response = roomService.findRooms();
//        return CommonResponse.toResponse(CommonCode.OK, response);
        CommonResponse commonResponse = CommonResponse.toResponse(CommonCode.OK, response);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    // 매물 검색 - 필터 이용
    @Operation(summary = "필터 검색 api", description = "필터에 해당하는 매물 리스트 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "필터 검색")
            }
    )
    @PostMapping("/search")
    public ResponseEntity<CommonResponse> findRoomByFilter(RoomRequestDto.FilterDto body) throws Exception {

        log.info("[API] findRoomByFilter");
        List<RoomResponseDto.RoomList> response = roomService.findRoomByFilter(body);

        log.info("Room : {}", response);

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

    // 사용자로부터 매물 신고 접수
    @Operation(summary = "매물 신고 api", description = "사용자가 매물을 신고하는 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "매물 신고")
            }
    )
    @PostMapping("/report")
    public CommonResponse roomReportReceived(String report, String roomId) {
        log.info("[API] Room Report");
        User user = authService.getUserFromSecurity();
        return roomService.reportReceived(report, user, roomId);
    }

    // 관리자가 매물 신고 처리
    @Operation(summary = "매물 신고 처리 api", description = "관리자가 매물 신고 처리하는 api")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "매물 신고 처리")
            }
    )
    @PatchMapping("/manager/report")
    public CommonResponse roomReport(@RequestParam("roomId") String roomId) {
        log.info("[API] Manager Room Report");
        return roomService.reportRoom(UUID.fromString(roomId));
    }

}