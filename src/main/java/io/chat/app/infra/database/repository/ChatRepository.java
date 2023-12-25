package io.chat.app.infra.database.repository;

import io.chat.app.infra.database.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query("{'$or':[{'to': ?0}, {'from': ?1}]}")
    List<Chat> getMessagesByUser(String to, String from);
}
