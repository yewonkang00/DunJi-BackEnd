package com.dungzi.backend.domain.chat.application;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.dao.UserChatRoomDao;
import com.dungzi.backend.domain.chat.dto.ChatRoomResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Service
public class ChatRoomService {
    private final ChatRoomDao chatRoomDao;
    private final UserChatRoomDao userChatRoomDao;
    private final AuthService authService;
    private final UserDao userDao;

    public List<ChatRoomResponseDto.UsersChatRooms> findChatRoomsBySeekType(ChatRoomType chatRoomType){
        List<ChatRoomResponseDto.UsersChatRooms> responseChatRooms = new ArrayList<>();

        User ownUser = authService.getUserFromSecurity();

        List<UserChatRoom> ownUserChatRooms = userChatRoomDao.findByUserAndChatRoomType(ownUser,chatRoomType);
        for (UserChatRoom ownUserChatRoom : ownUserChatRooms) {
            Optional<UserChatRoom> opponentUserChatRoom = getOpponentUserChatRoom(chatRoomType, ownUserChatRoom);

            //TODO 상대방이 없는 채팅인 경우 예외처리(로직상 잘못됨! 여기로 들어가면 안됨!
            if (opponentUserChatRoom.isEmpty()) {
                log.error("채팅방 조회시 상대방의 채팅방이 없음. 조회한 유저{}",ownUser.getNickname());
            }

            //TODO 추후 매물관련 데이터 추가
            responseChatRooms.add(ChatRoomResponseDto.UsersChatRooms.builder()
                    .chatRoomId(ownUserChatRoom.getChatRoom().getChatRoomId())
                    .opponentName(opponentUserChatRoom.get().getUser().getNickname())
                    .build());
        }
        log.debug("채팅방 조회");
        return responseChatRooms;
    }

    private Optional<UserChatRoom> getOpponentUserChatRoom(ChatRoomType chatRoomType, UserChatRoom ownUserChatRoom) {
        ChatRoom commonChatRoom = ownUserChatRoom.getChatRoom();
        ChatRoomType oppositeChatRoomType = ChatRoomType.findOppositeChatRoomType(chatRoomType);
        Optional<UserChatRoom> opponentUserChatRoom = userChatRoomDao.findByChatRoomAndChatRoomType(commonChatRoom,
                oppositeChatRoomType);
        return opponentUserChatRoom;
    }


    @Transactional
    public void deleteChatRoom(String chatRoomId) {
        ChatRoom chatRoom = chatRoomDao.findById(UUID.fromString(chatRoomId)).get();
        List<UserChatRoom> userChatRooms = userChatRoomDao.findByChatRoom(chatRoom);
        log.debug("채팅방 삭제");
        for (UserChatRoom userChatRoom : userChatRooms) {
            userChatRoomDao.deleteById(userChatRoom.getUserChatRoomId());
            log.debug("삭제된 채팅방 유저:{}",userChatRoom.getUser().getNickname());
        }
        chatRoomDao.deleteById(UUID.fromString(chatRoomId));


    }

    @Transactional
    public ChatRoom createChatRoom(String opponentNickName) {
        User ownUser = userDao.findById(authService.getUserFromSecurity().getUserId()).get();
        User opponentUser = userDao.findByNickname(opponentNickName).get();
        ChatRoom chatRoom = chatRoomDao.save(ChatRoom.builder()
                .build());

        saveUserChatRoom(chatRoom, ownUser, ChatRoomType.SEEK);
        saveUserChatRoom(chatRoom, opponentUser, ChatRoomType.GIVEN_OUT);
        log.debug("채팅방 생성 요청한 유저:{}",ownUser.getNickname());
        log.debug("채팅방 생성을 요청받은 유저:{}",opponentUser.getNickname());

        return chatRoom;
    }

    private void saveUserChatRoom(ChatRoom chatRoom, User user, ChatRoomType seek) {
        UserChatRoom userChatRoom = userChatRoomDao.save(UserChatRoom.builder()
                .chatRoom(chatRoom)
                .user(user)
                .chatRoomType(seek)
                .build());
    }

    @Transactional
    public Optional<ChatRoom> findExistRoom(String opponentNickName) {
        User opponentUser = userDao.findByNickname(opponentNickName).get();
        User ownUser = userDao.findById(authService.getUserFromSecurity().getUserId()).get();

        List<UserChatRoom> ownUserChatRooms = userChatRoomDao.findByUser(ownUser);
        List<UserChatRoom> opponentUserChatRooms = userChatRoomDao.findByUser(opponentUser);

        HashSet<ChatRoom> ownUserChatRoomSet = makeHashSet(ownUserChatRooms);

        for (UserChatRoom opponentUserChatRoom : opponentUserChatRooms) {
            if (ownUserChatRoomSet.contains(opponentUserChatRoom.getChatRoom())) {
                log.debug("이미 채팅방이 존재함");
                return Optional.of(opponentUserChatRoom.getChatRoom());
            }
        }
        log.debug("기존의 채팅방이 없어 새로 생성");
        return Optional.empty();

    }

    private HashSet<ChatRoom> makeHashSet(List<UserChatRoom> ownUserChatRooms) {
        HashSet<ChatRoom> hashSet = new HashSet<>();
        for (UserChatRoom ownUserChatRoom : ownUserChatRooms) {
            hashSet.add(ownUserChatRoom.getChatRoom());
        }
        return hashSet;
    }

}
