package io.chat.app.application.chat.services;

import io.chat.app.AppApplication;
import io.chat.app.application.chat.dtos.ChatResponseDTO;
import io.chat.app.application.chat.services.interfaces.IGetMessagesService;
import io.chat.app.infra.database.entity.Chat;
import io.chat.app.infra.database.repository.ChatRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetMessagesService implements IGetMessagesService {

    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ChatResponseDTO> getMessagesBySenderAndRecipient(String from, String to) {
        logger.info("Searching chat by ids");

        List<Chat> chatList = chatRepository.findMessagesBySenderAndRecipient(from, to, Sort.by(Sort.Direction.DESC, "createdAt"));

        logger.info("Found" + chatList.size() + " chat with ids " + from + " and " + to);

        return chatList.stream()
                .map(chatMessage -> modelMapper.map(chatMessage, ChatResponseDTO.class))
                .collect(Collectors.toList());
    }
}
