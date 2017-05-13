package eu.trustdemocracy.social.core.entities;

import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Relationship {

  private User originUser;
  private UUID targetUserId;
  private RelationshipType relationshipType;
  private RelationshipStatus relationshipStatus;
}
