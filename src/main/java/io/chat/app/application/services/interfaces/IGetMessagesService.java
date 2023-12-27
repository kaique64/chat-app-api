package io.chat.app.application.services.interfaces;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.SendMessageDTO;

import java.util.List;

public interface IGetMessagesService {
    public List<ChatResponseDTO> getMessagesByUserId(String userId);
}
