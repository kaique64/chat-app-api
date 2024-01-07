package io.chat.app.infra.http.controller;

import io.chat.app.application.chat.dtos.ChatNotificationDTO;
import io.chat.app.application.chat.dtos.ChatResponseDTO;
import io.chat.app.application.chat.dtos.CreateMessageDTO;
import io.chat.app.usecases.ChatUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatUseCase chatUseCase;

    @Autowired
    SimpMessagingTemplate template;

    @MessageMapping("/chat-message")
    @SendTo("/chat/messages")
    @PostMapping("/messages")
    public ChatResponseDTO sendMessage(@Payload @RequestBody @Valid CreateMessageDTO messageDTO) {
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
    public List<ChatResponseDTO> getMessagesByUser(@RequestParam String from, @RequestParam String to) {
        return chatUseCase.getMessagesByUserId(from, to);
    }
}
