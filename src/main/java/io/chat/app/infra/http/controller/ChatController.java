package io.chat.app.infra.http.controller;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.SendMessageDTO;
import io.chat.app.application.usecases.ChatUseCase;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatUseCase chatUseCase;

    @PostMapping
    public ResponseEntity<ChatResponseDTO> sendMessage(@RequestBody @Valid SendMessageDTO messageDTO) {
        return new ResponseEntity<ChatResponseDTO>(chatUseCase.sendMessage(messageDTO), HttpStatus.CREATED);
    }

}
