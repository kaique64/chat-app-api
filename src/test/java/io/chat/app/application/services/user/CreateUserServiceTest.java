package io.chat.app.application.services.user;

import io.chat.app.application.user.dtos.CreateUserDTO;
import io.chat.app.application.user.dtos.UserResponseDTO;
import io.chat.app.application.exceptions.AppException;
import io.chat.app.application.user.services.CreateUserService;
import io.chat.app.infra.database.entity.User;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {
    private final User user = new User();
    private final CreateUserDTO userDTO = new CreateUserDTO();
    private final UserResponseDTO responseDTO = new UserResponseDTO();

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserService createUserService;

    @BeforeEach
    public void setup() {
        userDTO.setName("John Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("123");

        user.setId("1");
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setPassword(user.getPassword());

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should save a new user with valid input data")
    public void test_save_new_user_with_valid_input_data() {
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.insert(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(responseDTO);

        UserResponseDTO responseDTO = createUserService.create(userDTO);

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getId());
        assertEquals("John Doe", responseDTO.getName());
    }

    @Test
    @DisplayName("Should hash the user's password before saving")
    public void test_hash_user_password_before_saving() {
        BCryptPasswordEncoder mockedPasswordEncoder = new BCryptPasswordEncoder();
        String mockedEncoding = mockedPasswordEncoder.encode(userDTO.getPassword());

        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.insert(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(responseDTO);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn(mockedEncoding);

        UserResponseDTO responseDTO = createUserService.create(userDTO);
        responseDTO.setPassword(userDTO.getPassword());

        assertNotNull(responseDTO);
        assertNotNull(responseDTO.getPassword());
        assertNotEquals("123", responseDTO.getPassword());
    }

    @Test
    @DisplayName("Saving a new user with non-unique email should throw AppException")
    public void test_saveUserWithNonUniqueEmail() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(new User()));

        AppException appException = assertThrows(AppException.class, () -> createUserService.create(userDTO));
        assertEquals("User already exists", appException.getMessage());
        verify(userRepository).findByEmail(userDTO.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw an exception if name is null or empty")
    public void test_throw_exception_if_name_is_null_or_empty() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName(null);
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("password123");

        CreateUserService createUserService = new CreateUserService();

        assertThrows(Exception.class, () -> createUserService.create(userDTO));
    }

    @Test
    @DisplayName("Should throw an exception if email is null or empty")
    public void test_throw_exception_if_email_is_null_or_empty() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail(null);
        userDTO.setPassword("password123");

        CreateUserService createUserService = new CreateUserService();

        assertThrows(Exception.class, () -> createUserService.create(userDTO));
    }

    @Test
    @DisplayName("Should throw an exception if password is null or empty")
    public void test_throw_exception_if_password_is_null_or_empty() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword(null);

        CreateUserService createUserService = new CreateUserService();

        assertThrows(Exception.class, () -> createUserService.create(userDTO));
    }

}
