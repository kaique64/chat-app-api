package io.chat.app.application.user.services;

import io.chat.app.application.exceptions.AppException;
import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.user.services.interfaces.ICreateUserService;
import io.chat.app.infra.database.entity.User;
import io.chat.app.infra.database.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CreateUserService implements ICreateUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO create(CreateUserDTO userDTO) {
        log.info("Encrypting password");

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        log.info("Password encrypted successfully");

        log.info("Searching for user with email " + userDTO.getEmail());

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            log.error("User with email " + userDTO.getEmail() + " already exists");
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        log.info("Inserting new user");

        User userCreated = userRepository.insert(modelMapper.map(userDTO, User.class));

        log.info("User inserted successfully");

        return modelMapper.map(userCreated, UserResponseDTO.class);
    }
}
