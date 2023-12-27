package io.chat.app.application.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponseDTO {

    private String id;

    private String from;

    private String to;

    private String message;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
