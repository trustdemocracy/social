package eu.trustdemocracy.social.core.models.request;

import eu.trustdemocracy.social.core.entities.RelationshipType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class RelationshipRequestDTO {

    private String originUserToken;
    private UUID targetUserId;
    private RelationshipType relationshipType;
}
