package io.chat.app.usecases;

import io.chat.app.application.exceptions.AppException;
import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.SignInUserDTO;
import io.chat.app.application.user.dtos.SignInUserResponseDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.user.services.AuthenticationService;
import io.chat.app.application.user.services.CreateUserService;
import io.chat.app.application.user.services.TokenService;
import io.chat.app.infra.database.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserUseCase {

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private AuthenticationService authenticationService;

    public UserResponseDTO create(CreateUserDTO userDTO) {
        return createUserService.create(userDTO);
    }

    public SignInUserResponseDTO signIn(SignInUserDTO userDTO) {
        return authenticationService.signIn(userDTO);
    }

}
