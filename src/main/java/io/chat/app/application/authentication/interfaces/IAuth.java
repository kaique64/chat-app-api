package io.chat.app.application.authentication.interfaces;

import io.chat.app.application.authentication.dtos.SignInDTO;
import io.chat.app.application.authentication.dtos.SignInResponseDTO;

public interface IAuth {
    SignInResponseDTO signIn(SignInDTO userDTO);
}
