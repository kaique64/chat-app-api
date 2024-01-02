package io.chat.app.application.authentication.interfaces;

import io.chat.app.application.authentication.dtos.SignInUserDTO;
import io.chat.app.application.authentication.dtos.SignInUserResponseDTO;

public interface IAuthentication {
    SignInUserResponseDTO signIn(SignInUserDTO userDTO);
}
