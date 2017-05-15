package eu.trustdemocracy.social.core.models.request;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TargetRelationshipRequestDTO {

  private UUID originUserId;
  private String targetUserToken;
}
