package io.chat.app.application.usecases;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.CreateMessageDTO;
import io.chat.app.application.services.GetMessagesService;
import io.chat.app.application.services.SaveMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatUseCase {

    @Autowired
    private SaveMessageService saveMessageService;

    @Autowired
    private GetMessagesService getMessagesService;

    public ChatResponseDTO saveMessage(CreateMessageDTO messageDTO) {
        return saveMessageService.saveMessage(messageDTO);
    }

    public List<ChatResponseDTO> getMessagesByUserId(String from, String to) {
        return getMessagesService.getMessagesByUserId(from, to);
    }

}
