package io.chat.app.infra.http.controller.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chat.app.AppApplication;
import io.chat.app.application.chat.dtos.ChatNotificationDTO;
import io.chat.app.application.chat.dtos.CreateMessageDTO;
import io.chat.app.infra.database.entity.User;
import io.chat.app.infra.database.repository.UserRepository;
import io.chat.app.util.MongoDBDropDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppApplication.class })
@WebAppConfiguration
public class SendMessageRouteTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MongoDBDropDatabase mongoDBDropDatabase;

    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    private static final User senderUser = new User();
    private static final User recipientUser = new User();
    private static final CreateMessageDTO messageDTO = new CreateMessageDTO();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ROUTE = "/api/chat/messages";
    private static final MediaType DEFAULT_MEDIATYPE_REQUEST = MediaType.APPLICATION_JSON;
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJrYWlxdWUzMjFAZ21haWwuY29tIiwiaWQiOiI2NTk5YTEzMjcwMTEyMjVmNTNjN2RjOGEiLCJleHAiOjE3MDQ2ODc2NzF9.IVqSA2vaJr0jO620kqWTiiuc5cmhosWYs_AW54gQGDI";

    @BeforeEach
    public void setup() throws Exception {
        mongoDBDropDatabase.drop();

        senderUser.setName("Kaique");
        senderUser.setEmail("kaique@gmail.com");
        senderUser.setPassword("123");

        recipientUser.setName("John");
        recipientUser.setEmail("johndoe@gmail.com");
        recipientUser.setPassword("321");

        userRepository.insert(senderUser);
        userRepository.insert(recipientUser);

        messageDTO.setSenderId(senderUser.getId());
        messageDTO.setRecipientId(recipientUser.getId());
        messageDTO.setMessage("Hello John");

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("When a valid messageDTO is provided, the method should save the message and return the sent message.")
    public void test_valid_messageDTO() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(messageDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.message").value("Hello John"));
    }

    @Test
    @DisplayName("When an invalid message is sent, it should return a validation error.")
    public void test_invalid_message_returns_validation_error() throws Exception {
        // Arrange
        CreateMessageDTO messageDTO = new CreateMessageDTO();
        messageDTO.setSenderId("");
        messageDTO.setRecipientId("John");
        messageDTO.setMessage("Hello");
        String jsonBody = objectMapper.writeValueAsString(messageDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("When the recipient is not found, it should return an error message.")
    public void test_recipient_not_found_returns_error_message() throws Exception {
        // Arrange
        CreateMessageDTO messageDTO = new CreateMessageDTO();
        messageDTO.setSenderId("Kaique");
        messageDTO.setRecipientId("nonExistentRecipientId");
        messageDTO.setMessage("Hello");
        String jsonBody = objectMapper.writeValueAsString(messageDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST));
    }

    @Test
    public void test_sender_not_found_returns_error_message() throws Exception {
        // Arrange
        CreateMessageDTO messageDTO = new CreateMessageDTO();
        messageDTO.setSenderId("nonExistentSenderId");
        messageDTO.setRecipientId("John");
        messageDTO.setMessage("Hello");
        String jsonBody = objectMapper.writeValueAsString(messageDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST));
    }
}
