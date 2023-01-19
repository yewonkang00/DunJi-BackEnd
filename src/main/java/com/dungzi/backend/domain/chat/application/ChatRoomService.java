package com.dungzi.backend.domain.chat.application;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.dao.UserChatRoomDao;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.application.UserService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
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


    @Transactional
    public void deleteChatRoom(String chatRoomId) {
        ChatRoom chatRoom = chatRoomDao.findById(UUID.fromString(chatRoomId)).get();
        List<UserChatRoom> userChatRooms = userChatRoomDao.findByChatRoom(chatRoom);
        for (UserChatRoom userChatRoom : userChatRooms) {
            userChatRoomDao.deleteById(userChatRoom.getUserChatRoomId());
        }
        chatRoomDao.deleteById(UUID.fromString(chatRoomId));
    }

    @Transactional
    public ChatRoom createChatRoom(String opponentNickName) {
        User ownUser = authService.getUserFromSecurity();
        User opponentUser = userDao.findByNickname(opponentNickName).get();
        ChatRoom chatRoom = chatRoomDao.save(ChatRoom.builder()
                .build());

        saveUserChatRoom(chatRoom, ownUser, ChatRoomType.SEEK);
        saveUserChatRoom(chatRoom, opponentUser, ChatRoomType.GIVEN_OUT);

        return chatRoom;
    }

    private void saveUserChatRoom(ChatRoom chatRoom, User user, ChatRoomType seek) {
        UserChatRoom userChatRoom = userChatRoomDao.save(UserChatRoom.builder()
                .chatRoom(chatRoom)
                .user(user)
                .chatRoomType(seek)
                .build());

        user.addUserChatRoom(userChatRoom);
    }

    @Transactional
    public Optional<ChatRoom> findExistRoom(String opponentNickName) {
        User opponentUser = userDao.findByNickname(opponentNickName).get();
        User ownUser = userDao.findById(authService.getUserFromSecurity().getUserId()).get();

        List<UserChatRoom> ownUserChatRooms = ownUser.getUserChatRooms();
        List<UserChatRoom> opponentUserChatRooms = opponentUser.getUserChatRooms();
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
