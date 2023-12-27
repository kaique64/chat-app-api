package io.chat.app.application.chat.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotificationDTO {
    private String id;
    private String senderId;
    private String recipientId;
    private String content;
}
