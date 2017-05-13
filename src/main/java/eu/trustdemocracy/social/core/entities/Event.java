package eu.trustdemocracy.social.core.entities;

import io.vertx.core.json.JsonObject;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Event {

  private UUID id;
  private UUID userId;
  private long timestamp;
  private JsonObject serializedContent;
}
