package io.chat.app.application.user.services.interfaces;

import io.chat.app.infra.database.entity.User;

public interface ITokenService {
    String generateToken(User user);
}
