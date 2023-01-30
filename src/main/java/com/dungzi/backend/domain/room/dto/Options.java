package com.dungzi.backend.domain.room.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Options {
    airConditioner("에어컨"),
    refrigerator("냉장고"),
    washingMachine("세탁기"),
    gasStove("가스레인지"),
    induction("인덕션"),
    microwave("전자레인지"),
    desk("책상"),
    bookcase("책장"),
    bed("침대"),
    closet("옷장"),
    sink("싱크대"),
    shoeCabinet("신발장");

    public String option;

    Options(String value) {
        this.option = value;
    }

}
