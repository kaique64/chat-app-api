package io.chat.app.usecases;

import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.authentication.dtos.SignInDTO;
import io.chat.app.application.authentication.dtos.SignInResponseDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.authentication.Auth;
import io.chat.app.application.user.services.CreateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUseCase {

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private Auth authenticationService;

    public UserResponseDTO create(CreateUserDTO userDTO) {
        return createUserService.create(userDTO);
    }

    public SignInResponseDTO signIn(SignInDTO userDTO) {
        return authenticationService.signIn(userDTO);
    }

}
