package com.dungzi.backend.domain.chat.application;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.dao.UserChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {
    @Mock
    private AuthService authService;
    @Mock
    private UserDao userDao;
    @Mock
    private ChatRoomDao chatRoomDao;
    @Mock
    private UserChatRoomDao userChatRoomDao;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    @DisplayName("채팅방 생성 로직 테스트")
    public void testCreateChatRoom() {
        // given
        String opponentNickName = "opponent";
        User ownUser = new User();
        User opponentUser = new User();
        ChatRoom expectedChatRoom = new ChatRoom();
        when(authService.getUserFromSecurity()).thenReturn(ownUser);
        when(userDao.findById(ownUser.getUserId())).thenReturn(Optional.of(ownUser));
        when(userDao.findByNickname(opponentNickName)).thenReturn(Optional.of(opponentUser));
        when(chatRoomDao.save(any(ChatRoom.class))).thenReturn(expectedChatRoom);

        // when
        ChatRoom actualChatRoom = chatRoomService.createChatRoom(opponentNickName);

        // then
        assertEquals(expectedChatRoom, actualChatRoom);
        verify(userChatRoomDao).save(argThat(ownUserChatRoom -> ownUserChatRoom.getChatRoom().equals(expectedChatRoom) && ownUserChatRoom.getUser().equals(ownUser) && ownUserChatRoom.getChatRoomType().equals(ChatRoomType.SEEK)));
        verify(userChatRoomDao).save(argThat(opponentUserChatRoom -> opponentUserChatRoom.getChatRoom().equals(expectedChatRoom) && opponentUserChatRoom.getUser().equals(opponentUser) && opponentUserChatRoom.getChatRoomType().equals(ChatRoomType.GIVEN_OUT)));
    }

    @Test
    @DisplayName("채팅방 존재 여부 테스트- 채팅방 존재하는 case")
    public void findAlreadyExistRoom() {
        // given
        String opponentNickName = "opponent";
        User ownUser = new User();
        User opponentUser = new User();
        ChatRoom expectedChatRoom = new ChatRoom();
        when(authService.getUserFromSecurity()).thenReturn(ownUser);
        when(userDao.findById(ownUser.getUserId())).thenReturn(Optional.of(ownUser));
        when(userDao.findByNickname(opponentNickName)).thenReturn(Optional.of(opponentUser));

        List<UserChatRoom> ownUserChatRooms = Arrays.asList(
                UserChatRoom.builder().user(ownUser).chatRoom(expectedChatRoom).chatRoomType(ChatRoomType.SEEK).build());
        List<UserChatRoom> opponentUserChatRooms = Arrays.asList(
                UserChatRoom.builder().user(opponentUser).chatRoom(expectedChatRoom).chatRoomType(ChatRoomType.GIVEN_OUT).build());
        when(userChatRoomDao.findByUser(ownUser)).thenReturn(ownUserChatRooms);
        when(userChatRoomDao.findByUser(opponentUser)).thenReturn(opponentUserChatRooms);
        // when
        Optional<ChatRoom> actualChatRoom = chatRoomService.findExistRoom(opponentNickName);
        // then
        assertTrue(actualChatRoom.isPresent());
        assertEquals(expectedChatRoom, actualChatRoom.get());
    }

    @Test
    @DisplayName("채팅방 존재 여부 테스트- 채팅방이 존재하지 않는 case")
    public void findNotExistRoom() {
        // given
        String opponentNickName = "opponent";
        User ownUser = new User();
        User opponentUser = new User();
        when(authService.getUserFromSecurity()).thenReturn(ownUser);
        when(userDao.findById(ownUser.getUserId())).thenReturn(Optional.of(ownUser));
        when(userDao.findByNickname(opponentNickName)).thenReturn(Optional.of(opponentUser));

        List<UserChatRoom> ownUserChatRooms = Arrays.asList();
        List<UserChatRoom> opponentUserChatRooms = Arrays.asList();
        when(userChatRoomDao.findByUser(ownUser)).thenReturn(ownUserChatRooms);
        when(userChatRoomDao.findByUser(opponentUser)).thenReturn(opponentUserChatRooms);
        // when
        Optional<ChatRoom> actualChatRoom = chatRoomService.findExistRoom(opponentNickName);
        // then
        assertTrue(actualChatRoom.isEmpty());
    }
}
