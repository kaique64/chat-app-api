package io.chat.app.application.user.services;

import io.chat.app.application.exceptions.AppException;
import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.user.services.interfaces.ICreateUserService;
import io.chat.app.infra.database.entity.User;
import io.chat.app.infra.database.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService implements ICreateUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO create(CreateUserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) throw new AppException("User already exists");

        User userCreated = userRepository.insert(modelMapper.map(userDTO, User.class));

        return modelMapper.map(userCreated, UserResponseDTO.class);
    }
}
