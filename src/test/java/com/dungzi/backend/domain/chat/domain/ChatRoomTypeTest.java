package com.dungzi.backend.domain.chat.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChatRoomTypeTest {

    @Test
    @DisplayName("채팅방 type String to ChatRoomType 찾는 로직 테스트")
    void findByType() {
        //given
        String chatRoomTypeStr = ChatRoomType.SEEK.getType();
        //when
        ChatRoomType chatRoomType = ChatRoomType.findByType(chatRoomTypeStr);
        //the
        assertThat(chatRoomType).isEqualTo(ChatRoomType.SEEK);
    }

    @Test
    @DisplayName("반대의 채팅방 타입 찾는 로직 테스트")
    void findOppositeChatRoomType() {
        //given
        ChatRoomType chatRoomType = ChatRoomType.SEEK;
        //when
        ChatRoomType oppositeChatRoomType = ChatRoomType.findOppositeChatRoomType(chatRoomType);
        //the
        assertThat(oppositeChatRoomType).isEqualTo(ChatRoomType.GIVEN_OUT);
    }
}