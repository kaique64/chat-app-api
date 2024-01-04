package io.chat.app.application.chat.services;

import io.chat.app.AppApplication;
import io.chat.app.application.chat.dtos.ChatResponseDTO;
import io.chat.app.application.chat.dtos.CreateMessageDTO;
import io.chat.app.application.chat.services.interfaces.ISaveMessageService;
import io.chat.app.infra.database.entity.Chat;
import io.chat.app.infra.database.repository.ChatRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveMessageService implements ISaveMessageService {

    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ChatResponseDTO saveMessage(CreateMessageDTO messageDTO) {
        logger.info("Inserting a new message");

        Chat sentMessage = chatRepository.insert(modelMapper.map(messageDTO, Chat.class));

        logger.info("Message inserted successfully");
        return modelMapper.map(sentMessage, ChatResponseDTO.class);
    }
}
