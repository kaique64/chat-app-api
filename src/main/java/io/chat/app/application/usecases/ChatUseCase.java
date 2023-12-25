package io.chat.app.application.usecases;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.SendMessageDTO;
import io.chat.app.application.services.SendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatUseCase {

    @Autowired
    private SendMessageService sendMessageService;

    public ChatResponseDTO sendMessage(SendMessageDTO messageDTO) {
        return sendMessageService.sendMessage(messageDTO);
    }

}
