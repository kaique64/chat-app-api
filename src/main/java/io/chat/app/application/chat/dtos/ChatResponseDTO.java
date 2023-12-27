package io.chat.app.application.chat.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponseDTO {

    private String id;

    private String senderId;

    private String recipientId;

    private String message;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
