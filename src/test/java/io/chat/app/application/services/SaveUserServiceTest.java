package io.chat.app.application.services;

import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.user.services.SaveUserService;
import io.chat.app.infra.database.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SaveUserServiceTest {
    private final User user = new User();
    private final CreateUserDTO userDTO = new CreateUserDTO();
    private final SaveUserService saveUserService = new SaveUserService();

    @BeforeEach
    public void setup() {
        userDTO.setName("John Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("123");

        user.setId("1");
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should save a new user with valid input data")
    public void test_save_new_user_with_valid_input_data() {
        UserResponseDTO responseDTO = saveUserService.save(userDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertEquals("John Doe", responseDTO.getName());
    }

    @Test
    @DisplayName("Should return a UserResponseDTO with the saved user's id and name")
    public void test_return_UserResponseDTO_with_saved_user_id_and_name() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("password123");

        SaveUserService saveUserService = new SaveUserService();
        UserResponseDTO responseDTO = saveUserService.save(userDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertEquals("John Doe", responseDTO.getName());
    }

    @Test
    @DisplayName("Should hash the user's password before saving")
    public void test_hash_user_password_before_saving() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("password123");

        SaveUserService saveUserService = new SaveUserService();
        UserResponseDTO responseDTO = saveUserService.save(userDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getPassword());
        assertNotEquals("password123", responseDTO.getPassword());
    }

    @Test
    @DisplayName("Should throw an exception if name is null or empty")
    public void test_throw_exception_if_name_is_null_or_empty() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName(null);
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("password123");

        SaveUserService saveUserService = new SaveUserService();

        assertThrows(Exception.class, () -> {
            saveUserService.save(userDTO);
        });
    }

    @Test
    @DisplayName("Should throw an exception if email is null or empty")
    public void test_throw_exception_if_email_is_null_or_empty() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail(null);
        userDTO.setPassword("password123");

        SaveUserService saveUserService = new SaveUserService();

        assertThrows(Exception.class, () -> {
            saveUserService.save(userDTO);
        });
    }

    @Test
    @DisplayName("Should throw an exception if password is null or empty")
    public void test_throw_exception_if_password_is_null_or_empty() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword(null);

        SaveUserService saveUserService = new SaveUserService();

        assertThrows(Exception.class, () -> {
            saveUserService.save(userDTO);
        });
    }

}
