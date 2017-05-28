package eu.trustdemocracy.social.core.interactors.relationship;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.entities.util.UserMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetRelationshipsResponseDTO;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;
import java.util.List;
import lombok.val;

public class GetRelationships implements Interactor<OriginRelationshipRequestDTO, GetRelationshipsResponseDTO> {

  private RelationshipRepository relationshipRepository;

  public GetRelationships(RelationshipRepository relationshipRepository) {
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  public GetRelationshipsResponseDTO execute(OriginRelationshipRequestDTO requestDTO) {
    val user = UserMapper.createEntity(requestDTO.getOriginUserToken());

    val targetId = requestDTO.getTargetUserId();
    List<Relationship> relationships;
    if (targetId != null) {
      relationships = relationshipRepository.getRelationships(user.getId(), requestDTO.getTargetUserId());
    } else {
      relationships = relationshipRepository.getRelationships(user.getId());
    }

    return RelationshipMapper.createResponse(relationships);
  }
}
