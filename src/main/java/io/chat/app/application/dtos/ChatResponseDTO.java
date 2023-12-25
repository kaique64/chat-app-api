package io.chat.app.application.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatResponseDTO {

    private String id;

    private String from;

    private String to;

    private String message;

}
