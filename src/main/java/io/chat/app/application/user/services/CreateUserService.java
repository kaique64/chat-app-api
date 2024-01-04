package io.chat.app.application.user.services;

import io.chat.app.AppApplication;
import io.chat.app.application.exceptions.AppException;
import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.user.services.interfaces.ICreateUserService;
import io.chat.app.infra.database.entity.User;
import io.chat.app.infra.database.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService implements ICreateUserService {

    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO create(CreateUserDTO userDTO) {
        logger.info("Encrypting password");

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        logger.info("Password encrypted successfully");

        logger.info("Searching for user with email " + userDTO.getEmail());

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            logger.error("User with email " + userDTO.getEmail() + " already exists");
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        logger.info("Inserting new user");

        User userCreated = userRepository.insert(modelMapper.map(userDTO, User.class));

        logger.info("User inserted successfully");

        return modelMapper.map(userCreated, UserResponseDTO.class);
    }
}
