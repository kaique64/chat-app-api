package io.chat.app.infra.http.controller;

import io.chat.app.application.dtos.ChatNotificationDTO;
import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.CreateMessageDTO;
import io.chat.app.application.usecases.ChatUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatUseCase chatUseCase;

    @Autowired
    SimpMessagingTemplate template;

    @MessageMapping("/chat-message")
    @SendTo("/chat/messages")
    public ChatResponseDTO sendMessage(@Payload @Valid CreateMessageDTO messageDTO) {
        ChatResponseDTO sentMessage = chatUseCase.saveMessage(messageDTO);
        template.convertAndSendToUser(
                sentMessage.getRecipientId(),
                "/chat/messages",
                new ChatNotificationDTO(
                        sentMessage.getId(),
                        sentMessage.getSenderId(),
                        sentMessage.getRecipientId(),
                        sentMessage.getMessage()
                )
        );
        return sentMessage;
    }

    @GetMapping("/messages")
    public List<ChatResponseDTO> getMessagesByUser(@RequestParam String userId) {
        return chatUseCase.getMessagesByUserId(userId);
    }
}
