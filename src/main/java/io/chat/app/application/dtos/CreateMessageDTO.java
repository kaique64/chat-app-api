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
    private String from;

    @NotNull
    @NotBlank
    @NotEmpty
    private String to;

    @NotNull
    @NotBlank
    @NotEmpty
    private String message;

}
