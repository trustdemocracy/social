package eu.trustdemocracy.social.core.entities.util;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.User;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetRelationshipsResponseDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import java.util.List;
import lombok.val;

public class RelationshipMapper {

  public static Relationship createEntity(OriginRelationshipRequestDTO relationshipRequestDTO) {
    return new Relationship()
        .setOriginUser(UserMapper.createEntity(relationshipRequestDTO.getOriginUserToken()))
        .setTargetUser(new User().setId(relationshipRequestDTO.getTargetUserId()));
  }

  public static Relationship createEntity(TargetRelationshipRequestDTO relationshipRequestDTO) {
    return new Relationship()
        .setOriginUser(new User().setId(relationshipRequestDTO.getOriginUserId()))
        .setTargetUser(UserMapper.createEntity(relationshipRequestDTO.getTargetUserToken()));
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

  public static GetRelationshipsResponseDTO createResponse(List<Relationship> relationships) {
    val responseDTO = new GetRelationshipsResponseDTO();
    for (val relationship : relationships) {
      responseDTO.getRelationships().add(createResponse(relationship));
    }
    return responseDTO;
  }
}
