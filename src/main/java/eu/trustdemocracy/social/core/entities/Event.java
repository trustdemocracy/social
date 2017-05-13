package eu.trustdemocracy.social.core.entities;

import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Event {

    private UUID id;
    private UUID userId;
    private long timestamp;
    private JsonObject serializedContent;
}
