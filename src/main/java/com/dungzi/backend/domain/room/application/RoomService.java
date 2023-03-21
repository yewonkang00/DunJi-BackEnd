package com.dungzi.backend.domain.room.application;

import com.dungzi.backend.domain.room.dao.*;
import com.dungzi.backend.domain.room.domain.*;
import com.dungzi.backend.domain.room.dto.*;
import com.dungzi.backend.domain.room.dto.RoomResponseDto.RoomRegist;
import com.dungzi.backend.domain.room.dto.RoomRequestDto.RegisterDto;
import com.dungzi.backend.domain.room.dto.enumType.OptionsEnum;
import com.dungzi.backend.domain.room.dto.enumType.UtilityEnum;
import com.dungzi.backend.domain.room.dto.enumType.roomStatus;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.common.response.CommonResponse;
import com.dungzi.backend.global.common.response.code.CommonCode;
import com.dungzi.backend.global.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomDao roomDao;
    private final RoomAddressDao roomAddressDao;
    private final RoomInfoDao roomInfoDao;
    private final RoomOptionDao roomOptionDao;
    private final RoomReportDao roomReportDao;
    private final FilterDao filterDao;
    private final FileUploadService fileUploadService;
    private RoomRequestDto roomRequestDto;
    private boolean fullOption = false;

    @Transactional
    public RoomRegist saveAction(RegisterDto requestDto, List<MultipartFile> files, User user) {

        log.info("[SERVICE] Room SaveAction");

        // Room 데이터 저장
        requestDto.setImage(files.size());

        Room savedRoom = roomDao.save(requestDto.toRoomEntity(user));
        requestDto.setRoom(savedRoom);
        log.info("[SERVICE] Room Save");

        // 공통으로 저장되어야 할 roomId
        UUID roomId = savedRoom.getRoomId();
        log.info("roomID: {}",roomId);
        requestDto.setRoomId(roomId);

        String status = roomStatus.active.toString();

        requestDto.setStatus(status);
        roomAddressDao.save(requestDto.toAddressEntity()); // RoomAddress 데이터 저장
        log.info("[SERVICE] RoomAddress Save");

        log.info("roomId : {}", requestDto.getRoomId());
        log.info("room : {}", requestDto.getRoom());
        roomOptionSave(requestDto); // RoomOption 데이터 저장
        log.info("[SERVICE] RoomOption Save");

        // RoomInfo 데이터 저장
        requestDto.setFullOption(fullOption);
        roomInfoDao.save(requestDto.toInfoEntity());
        log.info("[SERVICE] RoomInfo Save");

        // 이미지 저장
        fileUploadService.uploadRoomFile(roomId.toString(), files);
        log.info("[SERVICE] Room Image Save");

        // 응답 메시지
        RoomRegist responseDto = RoomRegist.builder()
                                    .roomId(roomId)
                                    .build();
        return responseDto;
    }

    public RoomOption roomOptionSave(RegisterDto requestDto) {
        int option_count = 0;  // full option(세탁기,냉장고,에어컨,가스레인지)인지 확인하기 위한 count

        // Option
        List<String> option = new ArrayList<>();

        if(requestDto.isAirConditioner()) {
            option.add(OptionsEnum.airConditioner.toString());
            option_count++;
        }
        if(requestDto.isRefrigerator()) {
            option.add(OptionsEnum.refrigerator.toString());
            option_count++;
        }
        if(requestDto.isWashingMachine()) {
            option.add(OptionsEnum.washingMachine.toString());
            option_count++;
        }
        if(requestDto.isGasStove()) {
            option.add(OptionsEnum.gasStove.toString());
            option_count++;
        }

        if(requestDto.isInduction()) option.add(OptionsEnum.induction.toString());
        if(requestDto.isMicrowave()) option.add(OptionsEnum.microwave.toString());
        if(requestDto.isDesk()) option.add(OptionsEnum.desk.toString());
        if(requestDto.isBookcase()) option.add(OptionsEnum.bookcase.toString());
        if(requestDto.isBed()) option.add(OptionsEnum.bed.toString());
        if(requestDto.isCloset()) option.add(OptionsEnum.closet.toString());
        if(requestDto.isSink()) option.add(OptionsEnum.sink.toString());
        if(requestDto.isShoeCabinet()) option.add(OptionsEnum.shoeCabinet.toString());

        String request_option = String.join(",", option);

        if(option_count == 4) {
            fullOption = true;
        }

        /*
        // Advantage
        //EnumSet<Advantage> advantage = EnumSet.noneOf(Advantage.class);
        List<String> advantage = new ArrayList<>();
        if(requestDto.isElevators()) advantage.add(Advantage.elevators.toString());
        if(requestDto.isParking()) advantage.add(Advantage.parking.toString());
        if(requestDto.isPets()) advantage.add(Advantage.pets.toString());
        if(requestDto.isWomenOnly()) advantage.add(Advantage.womenOnly.toString());
        if(requestDto.isLoan()) advantage.add(Advantage.loan.toString());
        */

        // Utility
        //EnumSet<Utility> utility = EnumSet.noneOf(Utility.class);
        log.info("***TEST : {}", UtilityEnum.electricity.getValue());
        List<String> utility = new ArrayList<>();
        if(requestDto.isElectricity()) utility.add(UtilityEnum.electricity.getValue());
        if(requestDto.isGas()) utility.add(UtilityEnum.gas.getValue());
        if(requestDto.isWater()) utility.add(UtilityEnum.water.getValue());
        if(requestDto.isInternet()) utility.add(UtilityEnum.internet.getValue());
        if(requestDto.isTv()) utility.add(UtilityEnum.tv.getValue());

        String request_utility = String.join(",", utility);

        RoomOption roomOption = requestDto.toOptionEntity(request_option, request_utility);
        log.info("[SERVICE] roomOption : {}", request_option);
        log.info("[SERVICE] roomUtility : {}", request_option);
        log.info("[SERVICE] roomOptionSave");
        return roomOptionDao.save(roomOption);
    }

    @Transactional
    public List<Room> findRooms() {
        return roomDao.findAll();
    }

    public Room getRoomByUuid(String uuid) throws RuntimeException{
        log.info("[SERVICE] getRoomByUuid");
        return roomDao.findByRoomId(UUID.fromString(uuid))
                .orElse(null);
    }

    public RoomResponseDto.RoomDetail findRoomDetail(UUID roomId) throws RuntimeException{
        log.info("[SERVICE] getRoomByUuid");
        Room room = roomDao.findRoomDetailByRoomId(roomId);

        RoomInfoDto roomInfoDto = RoomInfoDto.toDto(room.getRoomInfo());
        RoomAddressDto roomAddressDto = RoomAddressDto.toDto(room.getRoomAddress());

        List<String> option = Arrays.asList(room.getRoomOption().getOptions().split(","));

        log.info("option : {}", option);

        List<String> roomImage = new ArrayList<>();
        for(int i = 0; i < room.getImage(); i++) {
            String url = "https://dungzi-files.s3.ap-northeast-2.amazonaws.com/dungzi-files/room/" + room.getRoomId() + "/";
            if(i == 0) {
                roomImage.add(url+"main");
            }
            else {
                roomImage.add(url+i);
            }
        }

        log.info("image : {}", roomImage);
        log.info("regDate : {}", room.getRegDate());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String regDate = dtf.format(room.getRegDate());

        log.info("regDate : {}", regDate);

        RoomResponseDto.RoomDetail roomDetail = RoomResponseDto.toDto(room, regDate, roomInfoDto,
                                                                    roomAddressDto, option, roomImage);

        return roomDetail;
    }

    // 필터 없이 좌표만을 이용한 매물 검색
    public List<RoomResponseDto.RoomList> findRoomByAddress(Double startLongitude, Double startLatitude, Double endLongitude, Double endLatitude) throws RuntimeException{
        log.info("[SERVICE] findRoomByAddress");

        List<RoomAddress> list = roomAddressDao.findRoomByAddress(startLongitude, startLatitude, endLongitude, endLatitude);
        List<RoomResponseDto.RoomList> roomList = new ArrayList<RoomResponseDto.RoomList>();

        for(RoomAddress data : list) {
            RoomAddress room = data;
            RoomResponseDto.RoomList newRoom = RoomResponseDto.toDto(room);
            roomList.add(newRoom);
        }

        return roomList;
    }

    // 필터를 이용한 매물 검색
    public List<RoomResponseDto.RoomList> findRoomByFilter(RoomRequestDto.FilterDto request) {
        log.info("[SERVICE] findRoomByFilter");

        List<RoomAddress> list = filterDao.findRoomByFilter(request);
        List<RoomResponseDto.RoomList> roomList = new ArrayList<>();

        for(RoomAddress data : list) {
            RoomAddress room = data;
            RoomResponseDto.RoomList newRoom = RoomResponseDto.toDto(room);
            roomList.add(newRoom);
        }
        
        return roomList;
    }

    @Transactional
    public CommonResponse deleteRoom(UUID user, UUID roomId) {
        log.info("[SERVICE] Delete Room : {}", roomId);

        // TODO : 관리자 생성 시 관리자도 삭제 가능하도록 추가
        Optional<Room> searchRoom = roomDao.findByRoomId(roomId);
        log.info("search Room : {}", searchRoom.get().getRoomId());

        // ID에 해당하는 매물이 존재하는지 확인
        if(searchRoom.isPresent()) {
            Room room = searchRoom.get();
            log.info("userID : {}", room.getUser().getUserId());
            log.info("userID2 : {}", user);

            // User가 매물을 등록한 user와 일치하는 경우만 삭제 가능
            if(room.getUser().getUserId().equals(user)) {

                roomAddressDao.updateStatus(roomStatus.deleted.toString(), roomId);
                return CommonResponse.toResponse(CommonCode.OK);
            }
            return CommonResponse.toResponse(CommonCode.UNAUTHORIZED, "삭제 권한이 없습니다.");
        }
        return CommonResponse.toResponse(CommonCode.NOT_FOUND, "해당 매물이 존재하지 않습니다.");
    }

    // 사용자로부터 매물 신고 접수
    @Transactional
    public CommonResponse reportReceived(String report, User user, String roomId) {
        log.info("[SERCICE] Room Report Received : {}", roomId);

        Optional<Room> roomData = roomDao.findByRoomId(UUID.fromString(roomId));
        Room room = roomData.get();

        log.info("search Room : {}", room.getRoomId());

        RoomReportDto roomReportDto = RoomReportDto.builder()
                                        .user(user)
                                        .room(room)
                                        .build();

        // ID에 해당하는 매물이 존재하는지 확인
        if(roomData.isPresent()) {
            //Room room = searchRoom.get();
            if(report.equals("표시된정보와다름")) {
                roomReportDto.setReport(roomStatus.differentFromDisplayed.getType());
            } else if(report.equals("매물이나갔음")) {
                    roomReportDto.setReport(roomStatus.beingSold.getType());
            }

            roomReportDao.save(roomReportDto.toEntity(roomReportDto));

            return CommonResponse.toResponse(CommonCode.OK);
        }

        return CommonResponse.toResponse(CommonCode.NOT_FOUND, "해당 매물이 존재하지 않습니다.");
    }

    // 관리자가 매물 신고 처리
    @Transactional
    public CommonResponse reportRoom(UUID roomId) {
        log.info("[SERCICE] Report Room : {}", roomId);

        Optional<Room> searchRoom = roomDao.findByRoomId(roomId);
        log.info("search Room : {}", searchRoom.get().getRoomId());

        // ID에 해당하는 매물이 존재하는지 확인
        if(searchRoom.isPresent()) {
            //Room room = searchRoom.get();
            roomAddressDao.updateStatus(roomStatus.reported.toString(), roomId);

            return CommonResponse.toResponse(CommonCode.OK);
        }
        return CommonResponse.toResponse(CommonCode.NOT_FOUND, "해당 매물이 존재하지 않습니다.");
    }
}
