package eu.trustdemocracy.social.core.interactors.relationship.follow;

import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;
import lombok.val;

public class FollowUser implements
    Interactor<OriginRelationshipRequestDTO, RelationshipResponseDTO> {

  private RelationshipRepository relationshipRepository;

  public FollowUser(RelationshipRepository relationshipRepository) {
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  public RelationshipResponseDTO execute(OriginRelationshipRequestDTO relationshipRequestDTO) {
    val relationship = RelationshipMapper.createEntity(relationshipRequestDTO);
    relationship.setRelationshipType(RelationshipType.FOLLOW);
    relationship.setRelationshipStatus(RelationshipStatus.PENDING);
    return RelationshipMapper.createResponse(relationshipRepository.create(relationship));
  }
}
