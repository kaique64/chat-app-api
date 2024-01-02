package io.chat.app.application.token.services.interfaces;

import io.chat.app.infra.database.entity.User;

public interface ITokenService {
    String generateToken(User user);

    String getSubject(String token);

}
