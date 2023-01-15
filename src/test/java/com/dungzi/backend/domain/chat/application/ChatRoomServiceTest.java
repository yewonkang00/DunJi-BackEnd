package com.dungzi.backend.domain.chat.application;

import static org.assertj.core.api.Assertions.*;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.dao.UserChatRoomDao;
import com.dungzi.backend.domain.user.application.UserService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({ChatRoomService.class})
@DataJpaTest
class ChatRoomServiceTest {
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatRoomDao chatRoomDao;
    @Autowired
    private UserChatRoomDao userChatRoomDao;
    @Autowired
    private UserDao userDao;

    private User ownUser = User.builder().nickname("test").email("test@naver.com").build();
    private User opponentUser = User.builder().nickname("opponent").email("opponent@naver.com").build();

    @BeforeEach
    void saveUser() {
        userDao.save(opponentUser);
        userDao.save(ownUser);
    }


    @Test
    @DisplayName("채팅방 생성(기존에 없는경우)")
    void createChatRoom() {
        //given
        //when
        chatRoomService.createChatRoom(ownUser, opponentUser);
        //then
        UserChatRoom ownChatRoom = ownUser.getUserChatRooms().get(0);
        UserChatRoom opponentChatRoom = opponentUser.getUserChatRooms().get(0);
        assertThat(ownChatRoom.getChatRoom()).isEqualTo(opponentChatRoom.getChatRoom());
        assertThat(ownChatRoom.getChatRoomType()).isEqualTo(ChatRoomType.SEEK);
        assertThat(opponentChatRoom.getChatRoomType()).isEqualTo(ChatRoomType.GIVEN_OUT);
    }

    @Test
    @DisplayName("기존에 있던 채팅방찾기")
    void findExistRoom() {
        //given
        ChatRoom chatRoom = chatRoomService.createChatRoom(ownUser, opponentUser);
        //when
        Optional<ChatRoom> existRoom = chatRoomService.findExistRoom(ownUser, opponentUser);
        //then
        assertThat(existRoom.isPresent()).isTrue();
    }

    @Test
    @DisplayName("기존에 없는 채팅방을 찾기")
    void findNotExistRoom() {
        //given
        //when
        Optional<ChatRoom> existRoom = chatRoomService.findExistRoom(ownUser, opponentUser);
        //then
        assertThat(existRoom.isEmpty()).isTrue();
    }


}