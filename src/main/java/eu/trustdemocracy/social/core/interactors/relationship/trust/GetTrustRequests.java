package eu.trustdemocracy.social.core.interactors.relationship.trust;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.entities.util.UserMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetRelationshipsResponseDTO;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;
import java.util.List;
import lombok.val;

public class GetTrustRequests implements Interactor<TargetRelationshipRequestDTO, GetRelationshipsResponseDTO> {

  private RelationshipRepository relationshipRepository;

  public GetTrustRequests(RelationshipRepository relationshipRepository) {
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  public GetRelationshipsResponseDTO execute(TargetRelationshipRequestDTO requestDTO) {
    val user = UserMapper.createEntity(requestDTO.getTargetUserToken());
    List<Relationship> relationships = relationshipRepository
        .findByTargetId(user.getId(), RelationshipType.TRUST, RelationshipStatus.PENDING);
    return RelationshipMapper.createResponse(relationships);
  }
}
