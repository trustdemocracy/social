package eu.trustdemocracy.social.core.models.request;

import io.vertx.core.json.JsonObject;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EventRequestDTO {

  private UUID userId;
  private String username;
  private String type;
  private long timestamp;
  private JsonObject serializedContent;
}
