package com.dungzi.backend.domain.chat.application;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.dao.UserChatRoomDao;
import com.dungzi.backend.domain.user.domain.User;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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


    @Transactional
    public ChatRoom createChatRoom(User ownUser, User opponentUser) {
        ChatRoom chatRoom = chatRoomDao.save(ChatRoom.builder()
                .build());

        userChatRoomDao.save(UserChatRoom.builder()
                .chatRoom(chatRoom)
                .user(ownUser)
                .chatRoomType(ChatRoomType.SEEK)
                .build());

        userChatRoomDao.save(UserChatRoom.builder()
                .chatRoom(chatRoom)
                .user(opponentUser)
                .chatRoomType(ChatRoomType.GIVEN_OUT)
                .build());
        return chatRoom;
    }

    public Optional<ChatRoom> findExistRoom(User ownUser, User opponentUser) {
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
