package io.chat.app.application.user.services.interfaces;

import io.chat.app.application.user.dtos.SignInUserDTO;
import io.chat.app.application.user.dtos.SignInUserResponseDTO;

public interface IAuthenticationService {
    SignInUserResponseDTO signIn(SignInUserDTO userDTO);
}
