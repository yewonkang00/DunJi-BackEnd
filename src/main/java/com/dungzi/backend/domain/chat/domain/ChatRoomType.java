package com.dungzi.backend.domain.chat.domain;

import java.util.Arrays;

public enum ChatRoomType {
    SEEK("seek"),
    GIVEN_OUT("given_out");

    private String type;

    ChatRoomType(String type) {
        this.type = type;
    }

    public static ChatRoomType findByType(String type) {
        return Arrays.stream(values())
                .filter(value -> value.type.equals(type))
                .findAny()
                .orElse(null);
    }

    public static ChatRoomType findOppositeChatRoomType(ChatRoomType chatRoomType) {
        ChatRoomType opponentChatRoomType;
        if (chatRoomType.equals(ChatRoomType.SEEK)) {
            opponentChatRoomType = ChatRoomType.GIVEN_OUT;
        } else {
            opponentChatRoomType = ChatRoomType.SEEK;
        }
        return opponentChatRoomType;
    }

    public String getType() {
        return type;
    }
}
