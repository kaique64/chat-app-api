package io.chat.app.application.services.chat;

import io.chat.app.application.chat.dtos.ChatResponseDTO;
import io.chat.app.application.chat.services.GetMessagesService;
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
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetMessagesServiceTest {

    private final List<Chat> messages = new ArrayList<>();
    private final Chat chatMessage = new Chat();
    private final ChatResponseDTO message = new ChatResponseDTO();

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private GetMessagesService getMessagesService;

    @BeforeEach
    public void setup() {
        message.setId("message_id");
        message.setSenderId("user1");
        message.setRecipientId("user2");
        message.setMessage("Hello");

        chatMessage.setSenderId(message.getSenderId());
        chatMessage.setRecipientId(message.getRecipientId());
        chatMessage.setMessage(message.getMessage());

        messages.add(chatMessage);

        MockitoAnnotations.openMocks(this);
    }

    private Chat createChat(String from, String to, String message, LocalDateTime createdAt) {
        Chat chat = new Chat();
        chat.setSenderId(from);
        chat.setRecipientId(to);
        chat.setMessage(message);
        chat.setCreatedAt(createdAt);
        return chat;
    }

    private void mockModelMapperMapMethodToReturnNonNullValues () {
        when(modelMapper.map(any(), eq(ChatResponseDTO.class)))
                .thenAnswer(invocation -> {
                    Chat inputChat = invocation.getArgument(0);
                    ChatResponseDTO chatResponseDTO = new ChatResponseDTO();
                    chatResponseDTO.setSenderId(inputChat.getSenderId());
                    chatResponseDTO.setRecipientId(inputChat.getRecipientId());
                    chatResponseDTO.setMessage(inputChat.getMessage());
                    chatResponseDTO.setCreatedAt(inputChat.getCreatedAt());
                    return chatResponseDTO;
                });
    }

    @Test
    @DisplayName("The method returns a list of ChatResponseDTO objects when given valid 'from' and 'to' parameters")
    public void test_returns_list_of_ChatResponseDTO_objects_with_valid_parameters() {
        // Arrange
        String from = message.getSenderId();
        String to = message.getRecipientId();
        when(chatRepository.findMessagesBySenderAndRecipient(from, to, Sort.by(Sort.Direction.DESC, "createdAt"))).thenReturn(messages);

        // Act
        List<ChatResponseDTO> result = getMessagesService.getMessagesBySenderAndRecipient(from, to);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return a list of ChatResponseDTO objects in descending order of creation time")
    public void test_descending_order_of_creation_time() {
        // Arrange
        String from = "sender";
        String to = "recipient";

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteAgo = now.minusMinutes(1);
        LocalDateTime twoMinutesAgo = now.minusMinutes(2);

        Chat chat1 = createChat(from, to, "Hello", now);
        Chat chat2 = createChat(from, to, "Hi", oneMinuteAgo);
        Chat chat3 = createChat(from, to, "Hey", twoMinutesAgo);

        List<Chat> chatList = Arrays.asList(chat1, chat2, chat3);
        when(chatRepository.findMessagesBySenderAndRecipient(from, to, Sort.by(Sort.Direction.DESC, "createdAt"))).thenReturn(chatList);

        // Mock the modelMapper.map() method to return non-null ChatResponseDTO objects
        mockModelMapperMapMethodToReturnNonNullValues();

        // Act
        List<ChatResponseDTO> result = getMessagesService.getMessagesBySenderAndRecipient(from, to);

        // Assert
        assertEquals(chatList.size(), result.size());
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getCreatedAt().getMinute() > result.get(i + 1).getCreatedAt().getMinute());
        }
    }

    @Test
    public void test_returns_empty_list_when_no_messages_found() {
        // Arrange
        String from = message.getSenderId();
        String to = message.getRecipientId();

        // Act
        List<ChatResponseDTO> result = getMessagesService.getMessagesBySenderAndRecipient(from, to);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void test_throws_exception_when_given_null_from_parameter() {
        // Arrange
        GetMessagesService getMessagesService = new GetMessagesService();
        String from = null;
        String to = message.getRecipientId();

        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            getMessagesService.getMessagesBySenderAndRecipient(from, to);
        });
    }

    @Test
    public void test_throws_exception_when_given_null_to_parameter() {
        // Arrange
        GetMessagesService getMessagesService = new GetMessagesService();
        String from = message.getSenderId();
        String to = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> {
            getMessagesService.getMessagesBySenderAndRecipient(from, to);
        });
    }

    @Test
    public void test_returns_empty_list_when_given_invalid_parameters() {
        // Arrange
        String from = message.getSenderId();
        String to = "not valid user id";

        // Act
        List<ChatResponseDTO> result = getMessagesService.getMessagesBySenderAndRecipient(from, to);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}
