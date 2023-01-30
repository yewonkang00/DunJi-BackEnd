package com.dungzi.backend.domain.room.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Utility {

    electricity("전기세"),
    gas("가스비"),
    water("수도세"),
    internet("인터넷"),
    tv("TV");

    public String utility;

    Utility(String value) {
        this.utility = value;
    }

}
