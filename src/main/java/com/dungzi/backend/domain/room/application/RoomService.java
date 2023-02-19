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
//        requestDto.setRoomId(roomId.toString());

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
            option.add(OptionsEnum.airConditioner.getValue());
            option_count++;
        }
        if(requestDto.isRefrigerator()) {
            option.add(OptionsEnum.refrigerator.getValue());
            option_count++;
        }
        if(requestDto.isWashingMachine()) {
            option.add(OptionsEnum.washingMachine.getValue());
            option_count++;
        }
        if(requestDto.isGasStove()) {
            option.add(OptionsEnum.gasStove.getValue());
            option_count++;
        }
        if(requestDto.isInduction()) option.add(OptionsEnum.induction.getValue());
        if(requestDto.isMicrowave()) option.add(OptionsEnum.microwave.getValue());
        if(requestDto.isDesk()) option.add(OptionsEnum.desk.getValue());
        if(requestDto.isBookcase()) option.add(OptionsEnum.bookcase.getValue());
        if(requestDto.isBed()) option.add(OptionsEnum.bed.getValue());
        if(requestDto.isCloset()) option.add(OptionsEnum.closet.getValue());
        if(requestDto.isSink()) option.add(OptionsEnum.sink.getValue());
        if(requestDto.isShoeCabinet()) option.add(OptionsEnum.shoeCabinet.getValue());

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

                roomAddressDao.deleteRoom(roomStatus.deleted.toString(), roomId);
                return CommonResponse.toResponse(CommonCode.OK);
            }
            return CommonResponse.toResponse(CommonCode.UNAUTHORIZED, "삭제 권한이 없습니다.");
        }
        return CommonResponse.toResponse(CommonCode.NOT_FOUND, "해당 매물이 존재하지 않습니다.");
    }
}
