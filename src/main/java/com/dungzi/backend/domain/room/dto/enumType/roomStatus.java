package com.dungzi.backend.domain.room.dto.enumType;

public enum roomStatus {
    deleted("삭제됨"),
    active("거래가능"),
    transacted("거래완료"),
    reported("신고됨"),
    differentFromDisplayed("표시된정보와다름"),
    beingSold("매물이나갔음");


    private String type;
    roomStatus(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
