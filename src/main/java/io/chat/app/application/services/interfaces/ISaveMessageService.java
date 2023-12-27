package io.chat.app.application.services.interfaces;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.CreateMessageDTO;

public interface ISaveMessageService {

    public ChatResponseDTO saveMessage(CreateMessageDTO messageDTO);

}
