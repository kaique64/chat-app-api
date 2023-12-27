package io.chat.app.application.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CreateMessageDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    private String senderId;

    @NotNull
    @NotBlank
    @NotEmpty
    private String recipientId;

    @NotNull
    @NotBlank
    @NotEmpty
    private String message;

}
