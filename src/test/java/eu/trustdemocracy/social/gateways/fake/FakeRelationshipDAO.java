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
  public Relationship remove(Relationship relationship) {
    return relationships.remove(relationship) ? relationship : null;
  }

  @Override
  public Set<Relationship> getAllOriginRelationships(UUID id, RelationshipType relationshipType) {
    Set<Relationship> resultRelationships = new HashSet<>();
    for (val relationship : relationships) {
      if (relationship.getOriginUser().getId().equals(id)
      && relationship.getRelationshipType().equals(relationshipType)
          && relationship.getRelationshipStatus().equals(RelationshipStatus.ACCEPTED)) {
        resultRelationships.add(relationship);
      }
    }
    return resultRelationships;
  }

  @Override
  public List<Relationship> getRelationships(UUID originId, UUID targetId) {
    List<Relationship> result = new ArrayList<>();

    for (val relationship : relationships) {
      if ((relationship.getOriginUser().getId().equals(originId)
          && (targetId == null || relationship.getTargetUser().getId().equals(targetId)))
          || ((targetId == null || relationship.getOriginUser().getId().equals(targetId))
          && relationship.getTargetUser().getId().equals(originId))) {
        result.add(relationship);
      }
    }


    return result;
  }

  @Override
  public List<Relationship> findByTargetId(UUID id, RelationshipType relationshipType,
      RelationshipStatus relationshipStatus) {
    List<Relationship> relationshipsList = new ArrayList<>();
    for (val relationship : relationships) {
      if (relationship.getTargetUser().getId().equals(id)
          && relationship.getRelationshipType().equals(relationshipType)
          && relationship.getRelationshipStatus().equals(relationshipStatus)) {
        relationshipsList.add(relationship);
      }
    }
    return relationshipsList;
  }
}
