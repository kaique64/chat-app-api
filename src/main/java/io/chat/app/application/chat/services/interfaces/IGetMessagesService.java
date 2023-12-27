package io.chat.app.application.chat.services.interfaces;

import io.chat.app.application.chat.dtos.ChatResponseDTO;

import java.util.List;

public interface IGetMessagesService {
    public List<ChatResponseDTO> getMessagesBySenderAndRecipient(String from, String to);
}
