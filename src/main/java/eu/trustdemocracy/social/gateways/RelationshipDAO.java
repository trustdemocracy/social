package eu.trustdemocracy.social.gateways;

import eu.trustdemocracy.social.core.entities.Relationship;

public interface RelationshipDAO {
    Relationship create(Relationship relationship);
}
