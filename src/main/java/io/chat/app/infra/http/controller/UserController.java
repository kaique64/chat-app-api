package io.chat.app.infra.http.controller;

import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.SignInUserDTO;
import io.chat.app.application.user.dtos.SignInUserResponseDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.usecases.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserUseCase userUseCase;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid CreateUserDTO userDTO) {
        return new ResponseEntity<>(userUseCase.create(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInUserResponseDTO> signIn(@RequestBody @Valid SignInUserDTO userDTO) {
        return new ResponseEntity<>(userUseCase.signIn(userDTO), HttpStatus.OK);
    }

}
