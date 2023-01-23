package com.dungzi.backend.domain.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageResponseDto {
    private String chatRoomId;
    private String sender;
    private String content;
    private Date sendDate;


}
