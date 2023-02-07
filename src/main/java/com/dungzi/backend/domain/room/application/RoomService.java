package com.dungzi.backend.domain.room.application;

import com.dungzi.backend.domain.room.dao.RoomAddressDao;
import com.dungzi.backend.domain.room.dao.RoomDao;
import com.dungzi.backend.domain.room.dao.RoomInfoDao;
import com.dungzi.backend.domain.room.dao.RoomOptionDao;
import com.dungzi.backend.domain.room.domain.Room;
import com.dungzi.backend.domain.room.domain.RoomAddress;
import com.dungzi.backend.domain.room.domain.RoomInfo;
import com.dungzi.backend.domain.room.domain.RoomOption;
import com.dungzi.backend.domain.room.dto.*;
import com.dungzi.backend.domain.room.dto.RoomResponseDto.RoomRegist;
import com.dungzi.backend.domain.room.dto.RoomRequestDto.RegisterDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.domain.User;
import com.dungzi.backend.global.s3.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomDao roomDao;
    private final RoomAddressDao roomAddressDao;
    private final RoomInfoDao roomInfoDao;
    private final RoomOptionDao roomOptionDao;
    private final AuthService authService;

    @Transactional
    public RoomRegist saveAction(RegisterDto requestDto, List<MultipartFile> files, User user) {

        log.info("[Service] saveAction");
        //User user  = authService.getUserFromSecurity();
        log.info("[After]");
        //log.info("[userId]:{}",user);
        // Room 데이터 저장
//        RoomDto roomDto = RoomDto.builder()
//                        .userId(user.getUserId().toString())
//                        .title(requestDto.getTitle())
//                        .content(requestDto.getContent())
//                        .image(0)        //TODO : 날짜는 어떤 형식으로 입력할 지
//                        .heartNum(0)
//                        .build();

        Room room = Room.builder()
                .user(user)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .image(0)        //TODO : 날짜는 어떤 형식으로 입력할 지
                .heartNum(0)
                .build();
        log.info("[finish]");
        //requestDto.getRoom().setImage(files.size());
        //requestDto.getRoom().setHeartNum(0);
        Room roomRes = roomDao.save(room);

        // 공통으로 저장되어야 할 roomId
        UUID roomId = room.getRoomId();
        //String userId = requestDto.getUserId().toString();

        log.debug("[RoomService] Room Save. 매물 ID:{}, 등록한 유저:{}", roomId, user.getUserId());

//        // RoomAddress 데이터 저장
//        //requestDto.getAddress().setRoomId(roomId);
//        RoomAddress address = roomAddressDao.save(requestDto.getAddress().toEntity(roomId));
//
//        log.debug("[RoomService] RoomAddress Save. 매물 ID:{}, 등록한 유저:{}", roomId, userId);
//
//        // RoomInfo 데이터 저장
//        //requestDto.getInfo().setRoomId(roomId);
//        RoomInfo info = roomInfoDao.save(requestDto.getInfo().toEntity(roomId));
//
//        log.debug("[RoomService] RoomInfo Save. 매물 ID:{}, 등록한 유저:{}", roomId, userId);
//
//        // RoomOption 데이터 저장
//        requestDto.getOption().setRoomId(roomId);
//        //RoomOption option = roomOptionDao.save(requestDto.getOption().toEntity());
//        log.debug("[RoomService] RoomOption Save. 매물 ID:{}, 등록한 유저:{}", roomId, userId);

        // 이미지 저장
        try {
            FileUploadService.uploadRoomFile(roomId.toString(), files);
        } catch (Exception e) {
            log.error("[RoomService] FileUpload Error. 매물 ID:{}, 등록한 유저:{}", roomId, user.getUserId());
            e.printStackTrace();
        }


//        requestDto.setRoomId(roomId);
//        roomAddressSave(requestDto);
//        roomInfoSave(requestDto);
//        roomOptionSave(requestDto);

        RoomRegist responseDto = RoomRegist.builder()
                                    .roomId(roomId)
                                    .build();
        return responseDto;
    }


//    @Transactional
//    public Room roomSave(RoomDto requestDto) {
//        //requestDto.setUserId(user.getUserId());
//        return roomSave(requestDto.toEntity(requestDto));
//    }
//
//    @Transactional
//    public RoomAddress roomAddressSave(RoomRequestDto requestDto) {
//        RoomAddress address = roomAddressDao.save(requestDto.toRoomEntity());
//        return address;
//    }
//
//    @Transactional
//    public RoomInfo roomInfoSave(RoomRequestDto requestDto) {
//        return roomInfoSave(requestDto.toInfoEntity(requestDto));
//    }

    @Transactional
    public RoomOption roomOptionSave(RoomRequestDto.RoomRequestOptionDto requestDto) {

        // Option
        EnumSet<Options> option = EnumSet.noneOf(Options.class);
        if(requestDto.isAirConditioner()) option.add(Options.airConditioner);
        if(requestDto.isRefrigerator()) option.add(Options.refrigerator);
        if(requestDto.isWashingMachine()) option.add(Options.washingMachine);
        if(requestDto.isGasStove()) option.add(Options.gasStove);
        if(requestDto.isInduction()) option.add(Options.induction);
        if(requestDto.isMicrowave()) option.add(Options.microwave);
        if(requestDto.isDesk()) option.add(Options.desk);
        if(requestDto.isBookcase()) option.add(Options.bookcase);
        if(requestDto.isBed()) option.add(Options.bed);
        if(requestDto.isCloset()) option.add(Options.closet);
        if(requestDto.isSink()) option.add(Options.sink);
        if(requestDto.isShoeCabinet()) option.add(Options.shoeCabinet);

        // Advantage
        EnumSet<Advantage> advantage = EnumSet.noneOf(Advantage.class);
        if(requestDto.isElevators()) advantage.add(Advantage.elevators);
        if(requestDto.isParking()) advantage.add(Advantage.parking);
        if(requestDto.isPets()) advantage.add(Advantage.pets);
        if(requestDto.isWomenOnly()) advantage.add(Advantage.womenOnly);
        if(requestDto.isLoan()) advantage.add(Advantage.loan);

        // Utility
        EnumSet<Utility> utility = EnumSet.noneOf(Utility.class);
        if(requestDto.isElectricity()) utility.add(Utility.electricity);
        if(requestDto.isGas()) utility.add(Utility.gas);
        if(requestDto.isWater()) utility.add(Utility.water);
        if(requestDto.isInternet()) utility.add(Utility.internet);
        if(requestDto.isTv()) utility.add(Utility.tv);


        RoomOptionDto roomOptionDto = null;
        RoomOptionDto room = roomOptionDto.toEnumDto(requestDto.getRoomId(), option, utility, advantage);
//                RoomOptionDto.builder()
//                                    .roomId(requestDto.getRoomId())
//                                    .options(option)
//                                    .utility(utility)
//                                    .advantage(advantage)
//                                    .build();

//        return roomOptionSave(roomOptionDto.toEntity(roomOptionDto));

        log.info("[SERVICE] roomOptionSave");
        return roomOptionDao.save(room.toEntity(room));
    }

//    @Transactional
//    public Room roomSave(Room room) {
//        log.info("[SERVICE] roomSave");
//        return roomDao.save(room);
//    }
//
//    @Transactional
//    public RoomAddress roomAddressSave(RoomAddress roomAddress) {
//        log.info("[SERVICE] roomAddressSave");
//        return roomAddressDao.save(roomAddress);
//    }
//
//    @Transactional
//    public RoomInfo roomInfoSave(RoomInfo roomInfo) {
//        log.info("[SERVICE] roomInfoSave");
//        return roomInfoDao.save(roomInfo);
//    }
//
//    @Transactional
//    public RoomOption roomOptionSave(RoomOption roomOption) {
//        log.info("[SERVICE] roomOptionSave");
//        return roomOptionDao.save(roomOption);
//    }

    @Transactional
    public List<Room> findRooms() {
        return roomDao.findAll();
    }

    public Room getRoomByUuid(String uuid) throws RuntimeException{
        log.info("[SERVICE] getRoomByUuid");
        return roomDao.findByRoomId(UUID.fromString(uuid))
                .orElse(null);
    }
}
