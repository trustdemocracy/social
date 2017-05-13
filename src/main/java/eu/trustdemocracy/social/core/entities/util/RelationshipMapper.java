package eu.trustdemocracy.social.core.entities.util;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;

public class RelationshipMapper {

  public static Relationship createEntity(OriginRelationshipRequestDTO relationshipRequestDTO) {
    return new Relationship()
        .setOriginUser(UserMapper.createEntity(relationshipRequestDTO.getOriginUserToken()))
        .setTargetUser(new User().setId(relationshipRequestDTO.getTargetUserId()))
        .setRelationshipType(relationshipRequestDTO.getRelationshipType());
  }

  public static Relationship createEntity(TargetRelationshipRequestDTO relationshipRequestDTO) {
    return new Relationship()
        .setOriginUser(new User().setId(relationshipRequestDTO.getOriginUserId()))
        .setTargetUser(UserMapper.createEntity(relationshipRequestDTO.getTargetUserToken()))
        .setRelationshipType(relationshipRequestDTO.getRelationshipType());
  }

  public static RelationshipResponseDTO createResponse(Relationship relationship) {
    return new RelationshipResponseDTO()
        .setOriginUserId(relationship.getOriginUser().getId())
        .setOriginUserUsername(relationship.getOriginUser().getUsername())
        .setTargetUserId(relationship.getTargetUser().getId())
        .setTargetUserUsername(relationship.getTargetUser().getUsername())
        .setRelationshipType(relationship.getRelationshipType())
        .setRelationshipStatus(relationship.getRelationshipStatus());
  }
}
