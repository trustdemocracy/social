package eu.trustdemocracy.social.core.models.request;

import io.vertx.core.json.JsonObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class EventRequestDTO {

    private UUID userId;
    private long timestamp;
    private JsonObject serializedContent;
}
