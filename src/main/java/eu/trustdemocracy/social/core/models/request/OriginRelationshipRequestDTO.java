package eu.trustdemocracy.social.core.models.request;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OriginRelationshipRequestDTO {

  private String originUserToken;
  private UUID targetUserId;
}
