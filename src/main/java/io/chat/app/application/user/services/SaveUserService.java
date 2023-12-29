package io.chat.app.application.user.services;

import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.user.services.interfaces.ISaveUserService;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveUserService implements ISaveUserService {
    public UserResponseDTO save(CreateUserDTO userDTO) {
        return null;
    }
}
