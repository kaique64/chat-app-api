package io.chat.app.application.services;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.SendMessageDTO;
import io.chat.app.application.services.interfaces.ISendMessageService;
import io.chat.app.infra.database.entity.Chat;
import io.chat.app.infra.database.repository.ChatRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMessageService implements ISendMessageService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ChatResponseDTO sendMessage(SendMessageDTO messageDTO) {
        Chat sentMessage = chatRepository.insert(modelMapper.map(messageDTO, Chat.class));
        return modelMapper.map(sentMessage, ChatResponseDTO.class);
    }
}
