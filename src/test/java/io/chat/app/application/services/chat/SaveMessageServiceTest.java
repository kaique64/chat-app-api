package io.chat.app.application.services.chat;

import io.chat.app.application.chat.dtos.ChatResponseDTO;
import io.chat.app.application.chat.dtos.CreateMessageDTO;
import io.chat.app.application.chat.services.SaveMessageService;
import io.chat.app.infra.database.entity.Chat;
import io.chat.app.infra.database.entity.User;
import io.chat.app.infra.database.repository.ChatRepository;
import io.chat.app.infra.database.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SaveMessageServiceTest {

    private final ChatResponseDTO responseDTO = new ChatResponseDTO();
    private final Chat sentMessage = new Chat();
    private final User sender = new User();
    private final User recipient = new User();
    private final CreateMessageDTO messageDTO = new CreateMessageDTO();

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private SaveMessageService saveMessageService;

    @BeforeEach
    public void setup() {
        sender.setId("1");
        sender.setName("Kaique");
        sender.setEmail("kaique@gmail.com");
        sender.setPassword("123");

        recipient.setId("2");
        recipient.setName("John");
        recipient.setEmail("johndoe@gmail.com");
        recipient.setPassword("321");

        messageDTO.setSenderId(sender.getId());
        messageDTO.setRecipientId(recipient.getId());
        messageDTO.setMessage("Hello");

        sentMessage.setId("1");
        sentMessage.setSenderId(sender.getId());
        sentMessage.setRecipientId(recipient.getId());
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
        when(userRepository.findById(sender.getId())).thenReturn(Optional.of(sender));
        when(userRepository.findById(recipient.getId())).thenReturn(Optional.of(recipient));
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
