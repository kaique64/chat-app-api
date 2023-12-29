package io.chat.app.application.user.services.interfaces;

import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;

public interface ISaveUserService {
     UserResponseDTO save(CreateUserDTO userDTO);
}
