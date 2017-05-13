package eu.trustdemocracy.social.core.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Relationship {

    private User originUser;
    private UUID targetUserId;
    private RelationshipType relationshipType;
    private RelationshipStatus relationshipStatus;
}
