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
}
