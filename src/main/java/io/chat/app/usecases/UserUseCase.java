package io.chat.app.usecases;

import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.authentication.dtos.SignInUserDTO;
import io.chat.app.application.authentication.dtos.SignInUserResponseDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.authentication.Authentication;
import io.chat.app.application.user.services.CreateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUseCase {

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private Authentication authenticationService;

    public UserResponseDTO create(CreateUserDTO userDTO) {
        return createUserService.create(userDTO);
    }

    public SignInUserResponseDTO signIn(SignInUserDTO userDTO) {
        return authenticationService.signIn(userDTO);
    }

}
