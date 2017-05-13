package eu.trustdemocracy.social.core.models.response;

import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RelationshipResponseDTO {

  private UUID originUserId;
  private String originUserUsername;
  private UUID targetUserId;
  private RelationshipType relationshipType;
  private RelationshipStatus relationshipStatus;
}
