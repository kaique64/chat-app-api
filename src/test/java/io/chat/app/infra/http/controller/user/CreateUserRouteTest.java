package io.chat.app.infra.http.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chat.app.AppApplication;
import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.util.MongoDBDropDatabase;
import org.junit.jupiter.api.*;
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


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppApplication.class })
@WebAppConfiguration
public class CreateUserRouteTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MongoDBDropDatabase mongoDBDropDatabase;

    private MockMvc mockMvc;

    private static final CreateUserDTO userDTO = new CreateUserDTO();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String ROUTE = "/api/user";
    private static final MediaType DEFAULT_MEDIATYPE_REQUEST = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setup() throws Exception {
        mongoDBDropDatabase.drop();
        userDTO.setName("John Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("password");
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("Returns a UserResponseDTO with the same name, email, and password as the CreateUserDTO when a valid CreateUserDTO is provided")
    public void test_valid_createUserDTO_returnsUserResponseDTOWithSameFields() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(
                    post(ROUTE)
                            .content(jsonBody)
                            .contentType(DEFAULT_MEDIATYPE_REQUEST)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.email").value("johndoe@example.com"));
    }

    @Test
    @DisplayName("Returns a ResponseEntity with status code 400 when a CreateUserDTO with an empty name is provided")
    public void test_emptyName_createUserDTO_returnsResponseEntityWithStatusCode400() throws Exception {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("password");
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("Returns a ResponseEntity with status code 400 when a CreateUserDTO with an empty email is provided")
    public void test_emptyEmail_createUserDTO_returnsResponseEntityWithStatusCode400() throws Exception {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("");
        userDTO.setPassword("password");
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("Returns a ResponseEntity with status code 400 when a CreateUserDTO with an invalid email is provided")
    public void test_invalidEmail_createUserDTO_returnsResponseEntityWithStatusCode400() throws Exception {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("");
        userDTO.setPassword("password");
        String jsonBody = objectMapper.writeValueAsString(userDTO);

        mockMvc.perform(
                        post(ROUTE)
                                .content(jsonBody)
                                .contentType(DEFAULT_MEDIATYPE_REQUEST)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(DEFAULT_MEDIATYPE_REQUEST))
                .andExpect(jsonPath("$.errors").isArray());
    }
}
