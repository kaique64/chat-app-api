package io.chat.app.config.api.security.user.interfaces;

import io.chat.app.application.user.dtos.SignInUserDTO;
import io.chat.app.application.user.dtos.SignInUserResponseDTO;

public interface IAuthentication {
    SignInUserResponseDTO signIn(SignInUserDTO userDTO);
}
