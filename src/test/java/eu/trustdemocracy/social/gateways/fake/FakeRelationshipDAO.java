package eu.trustdemocracy.social.gateways.fake;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.gateways.RelationshipDAO;

import java.util.HashSet;
import java.util.Set;

public class FakeRelationshipDAO implements RelationshipDAO {

    private Set<Relationship> relationships = new HashSet<>();

    @Override
    public Relationship create(Relationship relationship) {
        relationships.add(relationship);
        return relationship;
    }
}
