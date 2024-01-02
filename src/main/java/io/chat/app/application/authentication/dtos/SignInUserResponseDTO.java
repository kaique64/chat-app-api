package io.chat.app.application.authentication.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignInUserResponseDTO {
    private String id;

    private String name;

    private String email;

    private String token;
}
