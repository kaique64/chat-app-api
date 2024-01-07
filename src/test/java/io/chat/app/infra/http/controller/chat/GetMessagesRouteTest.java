package io.chat.app.infra.http.controller.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chat.app.AppApplication;
import io.chat.app.infra.database.entity.Chat;
import io.chat.app.infra.database.entity.User;
import io.chat.app.infra.database.repository.ChatRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppApplication.class })
@WebAppConfiguration
public class GetMessagesRouteTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MongoDBDropDatabase mongoDBDropDatabase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    private MockMvc mockMvc;

    private static final User senderUser = new User();
    private static final User recipientUser = new User();
    private static final User recipientUser2 = new User();
    private static final Chat message = new Chat();
    private static final Chat newMessage = new Chat();
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

        recipientUser2.setName("Terry");
        recipientUser2.setEmail("terrydoe@gmail.com");
        recipientUser2.setPassword("321");

        userRepository.insert(senderUser);
        userRepository.insert(recipientUser);
        userRepository.insert(recipientUser2);

        message.setSenderId(senderUser.getId());
        message.setRecipientId(recipientUser.getId());
        message.setMessage("Hello John");

        newMessage.setSenderId(recipientUser.getId());
        newMessage.setRecipientId(senderUser.getId());
        newMessage.setMessage("Hello Kaique");

        chatRepository.insert(message);
        chatRepository.insert(newMessage);

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("Should return a list of ChatResponseDTO when 'from' and 'to' are valid and messages exist")
    public void test_validFromTo_MessagesExist() throws Exception {
        String routeWithQueryParams = ROUTE + "?from=" + senderUser.getId() + "&to=" + recipientUser.getId();
        mockMvc.perform(
                        get(routeWithQueryParams)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].message").value("Hello John"));
    }

    @Test
    @DisplayName("Should return a list of ChatResponseDTO when 'from' and 'to' are valid but no messages exist")
    public void test_validFromTo_NoMessagesExist() throws Exception {
        String routeWithQueryParams = ROUTE + "?from=" + senderUser.getId() + "&to=" + recipientUser2.getId();
        mockMvc.perform(
                        get(routeWithQueryParams)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should return a list of ChatResponseDTO in descending order of 'createdAt' when 'from' and 'to' are valid and messages exist")
    public void test_validFromTo_MessagesExist_DescendingOrder() throws Exception {
        String routeWithQueryParams = ROUTE + "?from=" + senderUser.getId() + "&to=" + recipientUser.getId();
        mockMvc.perform(
                        get(routeWithQueryParams)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + TOKEN)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].message").value(newMessage.getMessage()));
    }
}
