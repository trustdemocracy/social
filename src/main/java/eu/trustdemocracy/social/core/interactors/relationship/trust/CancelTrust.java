package eu.trustdemocracy.social.core.interactors.relationship.trust;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.interactors.exceptions.ResourceNotFoundException;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;
import eu.trustdemocracy.social.gateways.out.RankerGateway;
import lombok.val;

public class CancelTrust implements
    Interactor<TargetRelationshipRequestDTO, RelationshipResponseDTO> {

  private RelationshipRepository relationshipRepository;

  public CancelTrust(RelationshipRepository relationshipRepository, RankerGateway rankerGateway) {
    this.relationshipRepository = relationshipRepository;
  }

  @Override
  public RelationshipResponseDTO execute(
      TargetRelationshipRequestDTO targetRelationshipRequestDTO) {
    val relationship = RelationshipMapper.createEntity(targetRelationshipRequestDTO);
    relationship.setRelationshipType(RelationshipType.TRUST);

    val foundRelationship = relationshipRepository.find(relationship);

    if (foundRelationship == null) {
      throw new ResourceNotFoundException("The relationship must exist to be removed");
    }

    val followRelationship = new Relationship()
        .setOriginUser(foundRelationship.getOriginUser())
        .setTargetUser(foundRelationship.getTargetUser())
        .setRelationshipType(RelationshipType.FOLLOW);
    relationshipRepository.remove(followRelationship);

    return RelationshipMapper.createResponse(relationshipRepository.remove(foundRelationship));
  }
}
