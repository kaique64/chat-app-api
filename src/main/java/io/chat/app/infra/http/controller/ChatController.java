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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatUseCase chatUseCase;

    @Autowired
    SimpMessagingTemplate template;

    @MessageMapping("/chat-message")
    @SendTo("/chat/message/send")
    @PostMapping("/send")
    public ChatResponseDTO sendMessage(@Payload @Valid SendMessageDTO messageDTO) {
        template.convertAndSend("/chat/message", messageDTO);
        return chatUseCase.sendMessage(messageDTO);
    }

}
