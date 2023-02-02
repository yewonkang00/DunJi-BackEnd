package com.dungzi.backend.domain.chat.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class UserChatRoomDaoTest {


    @Autowired
    private UserDao userDao;
    @Autowired
    private UserChatRoomDao userChatRoomDao;
    @Autowired
    private ChatRoomDao chatRoomDao;



    @Test
    @DisplayName("채팅방 목록 select query테스트 - SeekType Case")
    public void selectChatRoomsSeekType() {
        // given
        User ownUser = User.builder().nickname("own").email("own@naver.com").build();
        User opponent = User.builder().nickname("opponentUser").email("opponent@naver.com").build();
        ChatRoom chatRoom = ChatRoom.builder().build();
        UserChatRoom ownUserChatRoom = UserChatRoom.builder().chatRoom(chatRoom).user(ownUser).chatRoomType(ChatRoomType.SEEK).build();
        UserChatRoom opponentUserChatRoom = UserChatRoom.builder().chatRoom(chatRoom).user(opponent)
                .chatRoomType(ChatRoomType.GIVEN_OUT).build();

        //save
        userDao.save(ownUser);
        userDao.save(opponent);
        chatRoomDao.save(chatRoom);
        userChatRoomDao.save(ownUserChatRoom);
        userChatRoomDao.save(opponentUserChatRoom);

        // when
        List<UserChatRoom> found = userChatRoomDao.findByUserAndChatRoomType(ownUser, ChatRoomType.SEEK);

        // then
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getUser()).isEqualTo(ownUser);
        assertThat(found.get(0).getChatRoomType()).isEqualTo(ChatRoomType.SEEK);
    }

    @Test
    @DisplayName("채팅방 목록 select query테스트 - GIVENOUT Case")
    public void selectChatRoomsGivenOut() {
        // given
        User ownUser = User.builder().nickname("own").email("own@naver.com").build();
        User opponent = User.builder().nickname("opponentUser").email("opponent@naver.com").build();
        ChatRoom chatRoom = ChatRoom.builder().build();
        UserChatRoom ownUserChatRoom = UserChatRoom.builder().chatRoom(chatRoom).user(ownUser).chatRoomType(ChatRoomType.GIVEN_OUT).build();
        UserChatRoom opponentUserChatRoom = UserChatRoom.builder().chatRoom(chatRoom).user(opponent)
                .chatRoomType(ChatRoomType.SEEK).build();

        //save
        userDao.save(ownUser);
        userDao.save(opponent);
        chatRoomDao.save(chatRoom);
        userChatRoomDao.save(ownUserChatRoom);
        userChatRoomDao.save(opponentUserChatRoom);

        // when
        List<UserChatRoom> found = userChatRoomDao.findByUserAndChatRoomType(ownUser, ChatRoomType.GIVEN_OUT);

        // then
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getUser()).isEqualTo(ownUser);
        assertThat(found.get(0).getChatRoomType()).isEqualTo(ChatRoomType.GIVEN_OUT);
    }
}
