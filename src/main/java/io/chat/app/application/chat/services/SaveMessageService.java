package io.chat.app.application.chat.services;

import io.chat.app.AppApplication;
import io.chat.app.application.chat.dtos.ChatResponseDTO;
import io.chat.app.application.chat.dtos.CreateMessageDTO;
import io.chat.app.application.chat.services.interfaces.ISaveMessageService;
import io.chat.app.infra.database.entity.Chat;
import io.chat.app.infra.database.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SaveMessageService implements ISaveMessageService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ChatResponseDTO saveMessage(CreateMessageDTO messageDTO) {
        log.info("Inserting a new message");

        Chat sentMessage = chatRepository.insert(modelMapper.map(messageDTO, Chat.class));

        log.info("Message inserted successfully");
        return modelMapper.map(sentMessage, ChatResponseDTO.class);
    }
}
