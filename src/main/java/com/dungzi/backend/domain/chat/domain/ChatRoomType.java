package com.dungzi.backend.domain.chat.domain;

import java.util.Arrays;

public enum ChatRoomType {
    SEEK("SEEK"),
    GIVEN_OUT("GIVEN_OUT");

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
}
