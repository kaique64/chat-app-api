package io.chat.app.usecases;

import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.SignInUserDTO;
import io.chat.app.application.user.dtos.SignInUserResponseDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.user.services.CreateUserService;
import io.chat.app.application.user.services.TokenService;
import io.chat.app.infra.database.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserUseCase {

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public UserResponseDTO create(CreateUserDTO userDTO) {
        return createUserService.create(userDTO);
    }

    public SignInUserResponseDTO signIn(SignInUserDTO userDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        User user = (User) authenticate.getPrincipal();

        String token = tokenService.generateToken(user);

        SignInUserResponseDTO signInUserResponseDTO = new SignInUserResponseDTO();
        signInUserResponseDTO.setId(user.getId());
        signInUserResponseDTO.setName(user.getName());
        signInUserResponseDTO.setEmail(user.getEmail());
        signInUserResponseDTO.setToken(token);

        return signInUserResponseDTO;
    }

}
