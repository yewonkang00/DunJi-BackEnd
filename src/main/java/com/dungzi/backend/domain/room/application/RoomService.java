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
import com.dungzi.backend.global.common.CommonCode;
import com.dungzi.backend.global.common.CommonResponse;
import com.dungzi.backend.global.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.dungzi.backend.domain.room.dto.enumType.type;

import java.text.SimpleDateFormat;
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
    private final FileUploadService fileUploadService;
    private RoomRequestDto roomRequestDto;
    private boolean fullOption = false;

    @Transactional
    public RoomRegist saveAction(RegisterDto requestDto, List<MultipartFile> files, User user) {

        log.info("[SERVICE] Room SaveAction");

        // Room 데이터 저장
//        UUID roomId = null;
        requestDto.setImage(files.size());
       // requestDto.setHeartNum(0);
//        Room room = Room.builder()
//                    .roomId(roomId)
//                    .user(user)
//                    .title(requestDto.getTitle())
//                    .content(requestDto.getContent())
//                    .image(files.size())        //TODO : 날짜는 어떤 형식으로 입력할 지
//                    .heartNum(0)
//                    .build();


        Room savedRoom = roomDao.save(requestDto.toRoomEntity(user));
        requestDto.setRoom(savedRoom);
        log.info("[SERVICE] Room Save");

        // 공통으로 저장되어야 할 roomId
        UUID roomId = savedRoom.getRoomId();
        log.info("roomID: {}",roomId);
        requestDto.setRoomId(roomId);

//        String roomType = type.roomType.valueOf(requestDto.getRoomType()).getValue();
//        String dealType = type.dealType.valueOf(requestDto.getDealType()).getValue();
//        String structType = type.structureType.valueOf(requestDto.getStructure()).getValue();
        String status = roomStatus.active.toString();

//        requestDto.setRoomType(roomType);
//        requestDto.setDealType(dealType);
//        requestDto.setStructure(structType);
        requestDto.setStatus(status);
   //     log.info("roomID2: {}",requestDto.getRoomId());
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
        //EnumSet<Options> option = EnumSet.noneOf(Options.class);
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
//        if(requestDto.isInduction()) option.add(OptionsEnum.induction.getValue());
//        if(requestDto.isMicrowave()) option.add(OptionsEnum.microwave.getValue());
//        if(requestDto.isDesk()) option.add(OptionsEnum.desk.getValue());
//        if(requestDto.isBookcase()) option.add(OptionsEnum.bookcase.getValue());
//        if(requestDto.isBed()) option.add(OptionsEnum.bed.getValue());
//        if(requestDto.isCloset()) option.add(OptionsEnum.closet.getValue());
//        if(requestDto.isSink()) option.add(OptionsEnum.sink.getValue());
//        if(requestDto.isShoeCabinet()) option.add(OptionsEnum.shoeCabinet.getValue());

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

//        RoomResponseDto.RoomDetail roomDetail = RoomResponseDto.RoomDetail.builder()
//                .roomId(room.getRoomId().toString())
//                .userName(room.getUser().getNickname())
//                .regDate(regDate)
//                .title(room.getTitle())
//                .content(room.getContent())
//                .heartNum(room.getHeartNum())
//                .roomInfo(roomInfoDto)
//                .roomAddress(roomAddressDto)
//                .option(option)
//                .utility(room.getRoomOption().getUtility())
//                .startedAt(room.getRoomOption().getStartedAt())
//                .finishedAt(room.getRoomOption().getFinishedAt())
//                .tenancyAgreement(room.getRoomOption().isTenancyAgreement())
//                .roomImage(roomImage)
//                .build();
        RoomResponseDto.RoomDetail roomDetail = RoomResponseDto.toDto(room, regDate, roomInfoDto,
                                                                    roomAddressDto, option, roomImage);

        return roomDetail;
    }

    public List<RoomResponseDto.RoomList> findRoomByAddress(Double startLongitude, Double startLatitude, Double endLongitude, Double endLatitude) throws RuntimeException{
        log.info("[SERVICE] findRoomByAddress");

        List<RoomAddress> list = roomAddressDao.findRoomByAddress(startLongitude, startLatitude, endLongitude, endLatitude);
        List<RoomResponseDto.RoomList> roomList = new ArrayList<RoomResponseDto.RoomList>();

        for(int i = 0; i < list.size(); i++) {
            RoomAddress room = list.get(i);
            RoomResponseDto.RoomList newRoom = RoomResponseDto.RoomList.builder()
                                                .roomId(room.getRoomId().toString())
                                                .longitude(room.getLongitude())
                                                .latitude(room.getLatitude())
                                                .sigungu(room.getSigungu())
                                                .dong(room.getDong())
                                                .priceUnit(room.getRoomInfo().getPriceUnit())
                                                .deposit(room.getRoomInfo().getDeposit())
                                                .price(room.getRoomInfo().getPrice())
                                                .roomType(room.getRoomInfo().getRoomType())
                                                .floor(room.getRoomInfo().getFloor())
                                                .roomSize(room.getRoomInfo().getRoomSize())
                                                .dealType(room.getRoomInfo().getDealType())
                                                .build();
            roomList.add(newRoom);
        }

        return roomList;
    }

    @Transactional
    public CommonResponse deleteRoom(UUID user, UUID roomId) {
        log.info("[SERCICE] Delete Room : {}", roomId);

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

    @Transactional
    public CommonResponse reportRoom(UUID roomId) {
        log.info("[SERCICE] Report Room : {}", roomId);

        Optional<Room> searchRoom = roomDao.findByRoomId(roomId);
        log.info("search Room : {}", searchRoom.get().getRoomId());

        // ID에 해당하는 매물이 존재하는지 확인
        if(searchRoom.isPresent()) {
            Room room = searchRoom.get();
            roomAddressDao.updateStatus(roomStatus.reported.toString(), roomId);
        }
        return CommonResponse.toResponse(CommonCode.NOT_FOUND, "해당 매물이 존재하지 않습니다.");
    }
}
