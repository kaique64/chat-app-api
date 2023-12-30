package io.chat.app.application.user.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignInUserDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    private String password;

}
