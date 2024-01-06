package io.chat.app.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoDBDropDatabase {
    @Autowired
    private MongoTemplate mongoTemplate;

    public void drop() {
        mongoTemplate.getDb().drop();
    }
}
