package io.chat.app.infra.http.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chat.app.AppApplication;
import io.chat.app.application.authentication.dtos.SignInDTO;
import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.services.CreateUserService;
import io.chat.app.infra.database.entity.User;
import io.chat.app.infra.database.repository.UserRepository;
import io.chat.app.util.MongoDBDropDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppApplication.class })
@WebAppConfiguration
public class SignInUserRouteTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MongoDBDropDatabase mongoDBDropDatabase;

    @Autowired
    private CreateUserService userService;

    private MockMvc mockMvc;

    private static final CreateUserDTO user = new CreateUserDTO();
    private static final SignInDTO signInUserDTO = new SignInDTO();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ROUTE = "/api/user/sign-in";
    private static final MediaType DEFAULT_MEDIATYPE_REQUEST = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setup() throws Exception {
        mongoDBDropDatabase.drop();
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("123");

        // Insert default user
        userService.create(user);

        signInUserDTO.setEmail("johndoe@example.com");
        signInUserDTO.setPassword("123");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("Returns a ResponseEntity with a SignInResponseDTO and HttpStatus.OK when valid SignInDTO is provided")
    public void test_valid_signInDTO_returns_response_with_OK_status() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(signInUserDTO);
        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"))
                .andExpect(jsonPath("$.token").isNotEmpty());

    }

    @Test
    @DisplayName("Returns a ResponseEntity with HttpStatus.UNAUTHORIZED when invalid email is provided")
    public void test_invalid_email_returns_response_with_UNAUTHORIZED_status() throws Exception {
        signInUserDTO.setEmail("invalid_email@example.com");
        String jsonBody = objectMapper.writeValueAsString(signInUserDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    @DisplayName("Returns a ResponseEntity with HttpStatus.UNAUTHORIZED when invalid password is provided")
    public void test_invalid_password_returns_response_with_UNAUTHORIZED_status() throws Exception {
        signInUserDTO.setPassword("invalid_password");
        String jsonBody = objectMapper.writeValueAsString(signInUserDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    public void test_empty_signInDTO_returns_response_with_BAD_REQUEST_status() throws Exception {
        SignInDTO signInDTO = new SignInDTO();
        String jsonBody = objectMapper.writeValueAsString(signInDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.status").value(400));
    }
}
