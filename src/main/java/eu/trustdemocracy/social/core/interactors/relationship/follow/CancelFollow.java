package eu.trustdemocracy.social.core.interactors.relationship.follow;

import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.interactors.exceptions.ResourceNotFoundException;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;
import eu.trustdemocracy.social.gateways.out.RankerGateway;
import lombok.val;

public class CancelFollow implements
    Interactor<TargetRelationshipRequestDTO, RelationshipResponseDTO> {

  private RelationshipRepository relationshipRepository;

  public CancelFollow(RelationshipRepository relationshipRepository, RankerGateway rankerGateway) {
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  public RelationshipResponseDTO execute(
      TargetRelationshipRequestDTO targetRelationshipRequestDTO) {
    val relationship = RelationshipMapper.createEntity(targetRelationshipRequestDTO);
    relationship.setRelationshipType(RelationshipType.FOLLOW);

    val foundRelationship = relationshipRepository.find(relationship);

    if (foundRelationship == null) {
      throw new ResourceNotFoundException("The relationship must exist to be removed");
    }

    return RelationshipMapper.createResponse(relationshipRepository.remove(foundRelationship));
  }
}
