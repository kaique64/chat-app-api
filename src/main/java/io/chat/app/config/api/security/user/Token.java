package io.chat.app.config.api.security.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.chat.app.application.user.services.interfaces.ITokenService;
import io.chat.app.infra.database.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class Token implements ITokenService {

    private static final String SECRET = "$2y$10$30MDVn3Dk7PJFkYAoVvJXeM0F/vMbH4dHThOuJABsdX8JiKNq2QTO";
    private static final String ZONE_ID = "-03:00";

    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withExpiresAt(LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of(ZONE_ID)))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public String getSubject(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build().verify(token)
                .getSubject();
    }
}
