package eu.trustdemocracy.social.gateways.out;

import eu.trustdemocracy.social.core.entities.Relationship;

public interface RankerGateway {

  void addRelationship(Relationship relationship);

  void removeRelationship(Relationship relationship);
}
