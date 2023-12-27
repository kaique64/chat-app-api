package io.chat.app.application.services;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.CreateMessageDTO;
import io.chat.app.application.services.interfaces.ISaveMessageService;
import io.chat.app.infra.database.entity.Chat;
import io.chat.app.infra.database.repository.ChatRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveMessageService implements ISaveMessageService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ChatResponseDTO saveMessage(CreateMessageDTO messageDTO) {
        Chat sentMessage = chatRepository.insert(modelMapper.map(messageDTO, Chat.class));
        return modelMapper.map(sentMessage, ChatResponseDTO.class);
    }
}
