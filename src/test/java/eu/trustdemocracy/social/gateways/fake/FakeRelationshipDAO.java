package eu.trustdemocracy.social.gateways.fake;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.val;

public class FakeRelationshipDAO implements RelationshipDAO {

  private List<Relationship> relationships = new ArrayList<>();

  @Override
  public Relationship create(Relationship relationship) {
    relationships.add(relationship);
    return relationship;
  }

  @Override
  public Relationship update(Relationship relationship) {
    for (int i = 0; i < relationships.size(); i++) {
      if (relationships.get(i).equals(relationship)) {
        relationships.set(i, relationship);
        break;
      }
    }
    return relationship;
  }

  @Override
  public Relationship find(Relationship relationship) {
    for (val storedRelationship : relationships) {
      if (storedRelationship.equals(relationship)) {
        return storedRelationship;
      }
    }
    return null;
  }

  @Override
  public Relationship remove(Relationship foundRelationship) {
    return relationships.remove(foundRelationship) ? foundRelationship : null;
  }

  @Override
  public Set<Relationship> getAllOriginRelationships(UUID id, RelationshipType relationshipType) {
    Set<Relationship> resultRelationships = new HashSet<>();
    for (val relationship : relationships) {
      if (relationship.getOriginUser().getId().equals(id)
      && relationship.getRelationshipType().equals(relationshipType)
          && relationship.getRelationshipStatus().equals(RelationshipStatus.ACEPTED)) {
        resultRelationships.add(relationship);
      }
    }
    return resultRelationships;
  }
}
