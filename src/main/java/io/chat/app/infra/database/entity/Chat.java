package io.chat.app.infra.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chats")
@Getter
@Setter
@NoArgsConstructor
public class Chat {

    @Id
    private String id;

    private String from;

    private String to;

    private String message;

}
