package io.chat.app.application.services;

import io.chat.app.application.dtos.ChatResponseDTO;
import io.chat.app.application.dtos.CreateMessageDTO;
import io.chat.app.infra.database.entity.Chat;
import io.chat.app.infra.database.repository.ChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SaveMessageServiceTest {

    private final ChatResponseDTO responseDTO = new ChatResponseDTO();
    private final Chat sentMessage = new Chat();
    private final CreateMessageDTO messageDTO = new CreateMessageDTO();

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SaveMessageService saveMessageService;

    @BeforeEach
    public void setup() {
        messageDTO.setSenderId("user1");
        messageDTO.setRecipientId("user2");
        messageDTO.setMessage("Hello");

        sentMessage.setId("1");
        sentMessage.setSenderId("user1");
        sentMessage.setRecipientId("user2");
        sentMessage.setMessage("Hello");

        responseDTO.setId(sentMessage.getId());
        responseDTO.setSenderId(sentMessage.getSenderId());
        responseDTO.setRecipientId(sentMessage.getRecipientId());
        responseDTO.setMessage(sentMessage.getMessage());

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Successfully sends a message from one user to another and returns a ChatResponseDTO object with the sent message details.")
    public void test_saveMessage_success() {
        when(modelMapper.map(messageDTO, Chat.class)).thenReturn(sentMessage);
        when(chatRepository.insert(any(Chat.class))).thenReturn(sentMessage);
        when(modelMapper.map(sentMessage, ChatResponseDTO.class)).thenReturn(responseDTO);

        // Act
        ChatResponseDTO result = saveMessageService.saveMessage(messageDTO);

        // Assert
        assertNotNull(result);
        assertEquals(sentMessage.getId(), result.getId());
        assertEquals(sentMessage.getSenderId(), result.getSenderId());
        assertEquals(sentMessage.getRecipientId(), result.getRecipientId());
        assertEquals(sentMessage.getMessage(), result.getMessage());
        // Verify interactions
        verify(modelMapper, times(1)).map(messageDTO, Chat.class);
        verify(chatRepository, times(1)).insert(any(Chat.class));
        verify(modelMapper, times(1)).map(sentMessage, ChatResponseDTO.class);

        // Verify specific arguments passed to the methods
        verify(modelMapper).map(eq(messageDTO), eq(Chat.class));
    }

    @Test
    public void test_throwExceptionIfMessageDTOIsNull() {
        when(saveMessageService.saveMessage(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> {
            saveMessageService.saveMessage(null);
        });
    }

}
