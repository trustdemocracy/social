package eu.trustdemocracy.social.gateways;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import java.util.Set;
import java.util.UUID;

public interface RelationshipDAO {

  Relationship create(Relationship relationship);

  Relationship update(Relationship relationship);

  Relationship find(Relationship relationship);

  Relationship remove(Relationship relationship);

  Set<Relationship> getAllOriginRelationships(UUID id, RelationshipType relationshipType);
}
