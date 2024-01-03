package io.chat.app.application.token.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.chat.app.application.token.services.interfaces.ITokenService;
import io.chat.app.infra.database.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService implements ITokenService {

    @Value("${jwt.secret}")
    private String SECRET;
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
