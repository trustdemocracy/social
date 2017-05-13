package eu.trustdemocracy.social.core.models.response;

import io.vertx.core.json.JsonObject;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class EventResponseDTO {

  private UUID id;
  private UUID userId;
  private long timestamp;
  private JsonObject serializedContent;
}
