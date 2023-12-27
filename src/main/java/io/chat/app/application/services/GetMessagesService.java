package io.chat.app.application.services;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.services.interfaces.IGetMessagesService;
import io.chat.app.infra.database.entity.Chat;
import io.chat.app.infra.database.repository.ChatRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetMessagesService implements IGetMessagesService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ChatResponseDTO> getMessagesByUserId(String from, String to) {
        List<Chat> chatList = chatRepository.findMessagesByUser(from, to);

        return chatList.stream()
                .map(chatMessage -> modelMapper.map(chatMessage, ChatResponseDTO.class))
                .collect(Collectors.toList());
    }
}