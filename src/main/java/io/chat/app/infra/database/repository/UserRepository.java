package io.chat.app.infra.database.repository;

import io.chat.app.infra.database.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
