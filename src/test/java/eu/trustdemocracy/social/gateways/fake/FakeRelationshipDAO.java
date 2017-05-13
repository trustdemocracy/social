package eu.trustdemocracy.social.gateways.fake;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import java.util.ArrayList;
import java.util.List;
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
}
