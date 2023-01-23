package com.dungzi.backend.domain.chat.dto;

import com.dungzi.backend.domain.chat.domain.ChatRoomType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

public class ChatRoomResponseDto {
    @Data
    @Builder
    public static class CreateChatRoom {
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = FromStringDeserializer.class)
        private UUID chatRoomId;
    }

    @Data
    @Builder
    public static class UsersChatRooms {
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = FromStringDeserializer.class)
        private UUID chatRoomId;

        private String opponentName;

//        "chatRoomId": "f189f4ae-af11-11e7-b252-186590cec0c1",
//                "opponentName": "유저1",
//                "roomAddress": "서울특별시 노원구 공릉동",
//                "lastMessage": "안녕하세요 직거래 매물....",
//                "lastMessageTime": "2시간전",
//                "roomImageUrl": "S3이미지 url 사용 예정?",
//                "notReadMessageCount": 10,
//                "roomDeposit": 1000,
//                "roomPrice": 50,
    }
}
