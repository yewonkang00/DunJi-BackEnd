package com.dungzi.backend.domain.chat.domain;

public enum ChatMessageType {
    MESSAGE("message"),
    ;
    private String type;
    ChatMessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
