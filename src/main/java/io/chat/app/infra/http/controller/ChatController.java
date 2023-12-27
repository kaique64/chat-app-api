package io.chat.app.infra.http.controller;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.SendMessageDTO;
import io.chat.app.application.usecases.ChatUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatUseCase chatUseCase;

    @Autowired
    SimpMessagingTemplate template;

    @MessageMapping("/chat-message")
    @SendTo("/chat/messages")
    @PostMapping("/messages/send")
    public ChatResponseDTO sendMessage(@Payload @RequestBody @Valid SendMessageDTO messageDTO) {
        template.convertAndSend("/chat/message", messageDTO);
        return chatUseCase.sendMessage(messageDTO);
    }

    @GetMapping("/messages")
    @SendTo("/chat/messages")
    public List<ChatResponseDTO> getMessagesByUser(@RequestParam String userId) {
        return chatUseCase.getMessagesByUserId(userId);
    }
}
