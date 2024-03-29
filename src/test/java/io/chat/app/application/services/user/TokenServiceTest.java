package io.chat.app.application.services.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.chat.app.application.authentication.dtos.SignInDTO;
import io.chat.app.application.authentication.dtos.SignInResponseDTO;
import io.chat.app.application.token.services.TokenService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration
public class TokenServiceTest {
    private static final String SECRET = "mocked-secret";
    private static final String ZONE_ID = "-03:00";
    private final User user = new User();
    private final SignInDTO userDTO = new SignInDTO();
    private final SignInResponseDTO responseDTO = new SignInResponseDTO();

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    public void setup() {
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("123");

        user.setId("1");
        user.setName("John Doe");
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());

        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setEmail(user.getEmail());

        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "SECRET", "jwt-secret-mock");
    }

    @Test
    @DisplayName("TokenService can generate a JWT token for a given User object")
    public void test_generateToken() {
        // Act
        String token = tokenService.generateToken(user);

        // Assert
        assertNotNull(token);
    }

    @Test
    @DisplayName("TokenService can extract the subject from a JWT token")
    public void test_getSubject() {
        // Arrange
        String token = tokenService.generateToken(user);

        // Act
        String subject = tokenService.getSubject(token);

        // Assert
        assertEquals("johndoe@example.com", subject);
    }

    @Test
    @DisplayName("TokenService generates a token that expires in 24 hours")
    public void test_tokenExpiration() {
        // Act
        String token = tokenService.generateToken(user);

        // Assert
        assertNotNull(token);
        // Verify token expiration
        DecodedJWT decodedToken = JWT.decode(token);
        Date expirationDate = decodedToken.getExpiresAt();
        LocalDateTime expirationDateTime = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expectedExpirationDateTime = LocalDateTime.now().plusHours(24);
        assertTrue(expirationDateTime.isBefore(expectedExpirationDateTime));
    }

    @Test
    @DisplayName("TokenService throws an exception when trying to extract the subject from an invalid JWT token")
    public void test_invalidTokenSubjectExtraction() {
        // Arrange
        String invalidToken = "invalid_token";

        // Act and Assert
        assertThrows(Exception.class, () -> {
            tokenService.getSubject(invalidToken);
        });
    }

    @Test
    @DisplayName("TokenService throws an exception when trying to extract the subject from a token with an invalid signature")
    public void test_invalidSignatureSubjectExtraction() {
        String token = tokenService.generateToken(user);
        String invalidToken = token + "invalid_signature";

        // Act and Assert
        assertThrows(Exception.class, () -> {
            tokenService.getSubject(invalidToken);
        });
    }

    @Test
    public void test_expiredTokenSubjectExtraction() {
        String expiredToken = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(LocalDateTime.now().minusHours(24).toInstant(ZoneOffset.of(ZONE_ID)))
                .sign(Algorithm.HMAC256(SECRET));

        // Act and Assert
        assertThrows(Exception.class, () -> {
            tokenService.getSubject(expiredToken);
        });
    }

}
