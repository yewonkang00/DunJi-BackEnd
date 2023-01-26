package com.dungzi.backend.domain.chat.application;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dungzi.backend.domain.chat.dao.ChatRoomDao;
import com.dungzi.backend.domain.chat.dao.UserChatRoomDao;
import com.dungzi.backend.domain.chat.domain.ChatRoom;
import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.dungzi.backend.domain.chat.domain.UserChatRoom;
import com.dungzi.backend.domain.chat.dto.ChatRoomResponseDto;
import com.dungzi.backend.domain.user.application.AuthService;
import com.dungzi.backend.domain.user.dao.UserDao;
import com.dungzi.backend.domain.user.domain.User;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        verify(userChatRoomDao).save(argThat(ownUserChatRoom -> ownUserChatRoom.getChatRoom().equals(expectedChatRoom)
                && ownUserChatRoom.getUser().equals(ownUser) && ownUserChatRoom.getChatRoomType()
                .equals(ChatRoomType.SEEK)));
        verify(userChatRoomDao).save(
                argThat(opponentUserChatRoom -> opponentUserChatRoom.getChatRoom().equals(expectedChatRoom)
                        && opponentUserChatRoom.getUser().equals(opponentUser) && opponentUserChatRoom.getChatRoomType()
                        .equals(ChatRoomType.GIVEN_OUT)));
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
                UserChatRoom.builder().user(ownUser).chatRoom(expectedChatRoom).chatRoomType(ChatRoomType.SEEK)
                        .build());
        List<UserChatRoom> opponentUserChatRooms = Arrays.asList(
                UserChatRoom.builder().user(opponentUser).chatRoom(expectedChatRoom)
                        .chatRoomType(ChatRoomType.GIVEN_OUT).build());
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

    @Test
    @DisplayName("채팅방 삭제 로직 테스트")
    public void testDeleteChatRoom() {
        // given
        String chatRoomId = "123e4567-e89b-12d3-a456-426655440000";
        String ownUserChatRoomId = "123e4567-e89b-12d3-a456-426655440001";
        String opponentUserChatRoomID = "123e4567-e89b-12d3-a456-426655440002";
        ChatRoom chatRoom = new ChatRoom();
        UserChatRoom ownUserChatRoom = UserChatRoom.builder().userChatRoomId(UUID.fromString(ownUserChatRoomId))
                .chatRoom(chatRoom)
                .user(User.builder().nickname("test").build())
                .build();
        UserChatRoom opponentUserChatRoom = UserChatRoom.builder().userChatRoomId(UUID.fromString(opponentUserChatRoomID))
                .chatRoom(chatRoom)
                .user(User.builder().nickname("test2").build())
                .build();
        List<UserChatRoom> haveSameChatRoom = Arrays.asList(ownUserChatRoom, opponentUserChatRoom);
        when(chatRoomDao.findById(UUID.fromString(chatRoomId))).thenReturn(Optional.of(chatRoom));
        when(userChatRoomDao.findByChatRoom(chatRoom)).thenReturn(haveSameChatRoom);
        // when
        chatRoomService.deleteChatRoom(chatRoomId);
        // then
        verify(userChatRoomDao, times(1)).deleteById(ownUserChatRoom.getUserChatRoomId());
        verify(userChatRoomDao, times(1)).deleteById(opponentUserChatRoom.getUserChatRoomId());
        verify(chatRoomDao, times(1)).deleteById(UUID.fromString(chatRoomId));
    }


    @Test
    @DisplayName("채팅방 조회 테스트-(구하는방 타입, 내놓은방 타입)")
    public void testFindChatRoomsBySeekType() {
        //given
        ChatRoomType chatRoomType = ChatRoomType.SEEK;
        User ownUser = User.builder().userId(UUID.randomUUID()).nickname("ownUser").build();
        ChatRoom commonChatRoom = ChatRoom.builder().chatRoomId(UUID.randomUUID()).build();
        User opponentUser = User.builder().userId(UUID.randomUUID()).nickname("opponentUser").build();
        UserChatRoom ownUserChatRoom = UserChatRoom.builder()
                .user(ownUser)
                .chatRoom(commonChatRoom)
                .chatRoomType(chatRoomType)
                .build();
        UserChatRoom opponentUserChatRoom = UserChatRoom.builder()
                .user(opponentUser)
                .chatRoom(commonChatRoom)
                .chatRoomType(ChatRoomType.findOppositeChatRoomType(chatRoomType))
                .build();
        List<UserChatRoom> ownUserChatRooms = List.of(ownUserChatRoom);

        when(authService.getUserFromSecurity()).thenReturn(ownUser);
        when(userChatRoomDao.findByUserAndChatRoomType(ownUser, chatRoomType)).thenReturn(ownUserChatRooms);
        when(userChatRoomDao.findByChatRoomAndChatRoomType(commonChatRoom, ChatRoomType.findOppositeChatRoomType(chatRoomType))).thenReturn(Optional.of(opponentUserChatRoom));

        //when
        List<ChatRoomResponseDto.UsersChatRooms> result = chatRoomService.findChatRoomsBySeekType(chatRoomType);

        //then
        assertEquals(1, result.size());
        ChatRoomResponseDto.UsersChatRooms resultChatRooms = result.get(0);
        assertEquals(commonChatRoom.getChatRoomId(), resultChatRooms.getChatRoomId());
        assertEquals(opponentUser.getNickname(), resultChatRooms.getOpponentName());
    }


}
