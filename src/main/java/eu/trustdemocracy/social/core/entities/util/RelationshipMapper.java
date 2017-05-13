package eu.trustdemocracy.social.core.entities.util;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.models.request.RelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;

public class RelationshipMapper {

  public static Relationship createEntity(RelationshipRequestDTO relationshipRequestDTO) {
    return new Relationship()
        .setOriginUser(UserMapper.createEntity(relationshipRequestDTO.getOriginUserToken()))
        .setTargetUserId(relationshipRequestDTO.getTargetUserId())
        .setRelationshipType(relationshipRequestDTO.getRelationshipType());
  }

  public static RelationshipResponseDTO createResponse(Relationship relationship) {
    return new RelationshipResponseDTO()
        .setOriginUserId(relationship.getOriginUser().getId())
        .setOriginUserUsername(relationship.getOriginUser().getUsername())
        .setTargetUserId(relationship.getTargetUserId())
        .setRelationshipType(relationship.getRelationshipType())
        .setRelationshipStatus(relationship.getRelationshipStatus());
  }
}
