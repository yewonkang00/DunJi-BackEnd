package com.dungzi.backend.domain.chat.application;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.dao.UserChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
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
        // Arrange
        String opponentNickName = "opponent";
        User ownUser = new User();
        User opponentUser = new User();
        ChatRoom expectedChatRoom = new ChatRoom();
        when(authService.getUserFromSecurity()).thenReturn(ownUser);
        when(userDao.findById(ownUser.getUserId())).thenReturn(Optional.of(ownUser));
        when(userDao.findByNickname(opponentNickName)).thenReturn(Optional.of(opponentUser));
        when(chatRoomDao.save(any(ChatRoom.class))).thenReturn(expectedChatRoom);

        // Act
        ChatRoom actualChatRoom = chatRoomService.createChatRoom(opponentNickName);

        // Assert
        assertEquals(expectedChatRoom, actualChatRoom);
        verify(userChatRoomDao).save(argThat(ownUserChatRoom -> ownUserChatRoom.getChatRoom().equals(expectedChatRoom) && ownUserChatRoom.getUser().equals(ownUser) && ownUserChatRoom.getChatRoomType().equals(
                ChatRoomType.SEEK)));
        verify(userChatRoomDao).save(argThat(opponentUserChatRoom -> opponentUserChatRoom.getChatRoom().equals(expectedChatRoom) && opponentUserChatRoom.getUser().equals(opponentUser) && opponentUserChatRoom.getChatRoomType().equals(ChatRoomType.GIVEN_OUT)));
    }
}
