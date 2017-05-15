package eu.trustdemocracy.social.core.interactors.relationship.follow;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.entities.util.UserMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetRelationshipsResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import java.util.List;
import lombok.val;

public class GetFollowRequests implements
    Interactor<TargetRelationshipRequestDTO, GetRelationshipsResponseDTO> {

  private RelationshipDAO relationshipDAO;

  public GetFollowRequests(RelationshipDAO relationshipDAO) {
    this.relationshipDAO = relationshipDAO;
  }

  @Override
  public GetRelationshipsResponseDTO execute(TargetRelationshipRequestDTO requestDTO) {
    val user = UserMapper.createEntity(requestDTO.getTargetUserToken());
    List<Relationship> relationships = relationshipDAO
        .findByTargetId(user.getId(), RelationshipType.FOLLOW, RelationshipStatus.PENDING);
    return RelationshipMapper.createResponse(relationships);
  }
}
