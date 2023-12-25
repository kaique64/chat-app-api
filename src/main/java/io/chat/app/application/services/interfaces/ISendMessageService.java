package io.chat.app.application.services.interfaces;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.SendMessageDTO;

public interface ISendMessageService {

    public ChatResponseDTO sendMessage(SendMessageDTO messageDTO);

}
