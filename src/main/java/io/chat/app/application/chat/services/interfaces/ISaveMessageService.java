package io.chat.app.application.chat.services.interfaces;

import io.chat.app.application.chat.dtos.ChatResponseDTO;
import io.chat.app.application.chat.dtos.CreateMessageDTO;

public interface ISaveMessageService {

    public ChatResponseDTO saveMessage(CreateMessageDTO messageDTO);

}
