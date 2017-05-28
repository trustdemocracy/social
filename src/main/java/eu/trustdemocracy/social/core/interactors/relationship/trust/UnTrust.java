package eu.trustdemocracy.social.core.interactors.relationship.trust;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.interactors.exceptions.ResourceNotFoundException;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.out.RankerGateway;
import eu.trustdemocracy.social.gateways.repositories.RelationshipRepository;
import lombok.val;

public class UnTrust implements Interactor<OriginRelationshipRequestDTO, RelationshipResponseDTO> {

  private RelationshipRepository relationshipRepository;
  private RankerGateway rankerGateway;

  public UnTrust(RelationshipRepository relationshipRepository, RankerGateway rankerGateway) {
    this.relationshipRepository = relationshipRepository;
    this.rankerGateway = rankerGateway;
  }

  @Override
  public RelationshipResponseDTO execute(
      OriginRelationshipRequestDTO originRelationshipRequestDTO) {
    val relationship = RelationshipMapper.createEntity(originRelationshipRequestDTO);
    relationship.setRelationshipType(RelationshipType.TRUST);

    val foundRelationship = relationshipRepository.find(relationship);

    if (foundRelationship == null) {
      throw new ResourceNotFoundException("The relationship must exist to be removed");
    }

    if (foundRelationship.getRelationshipStatus().equals(RelationshipStatus.ACCEPTED)) {
      rankerGateway.removeRelationship(foundRelationship);
    }

    val followRelationship = new Relationship()
        .setOriginUser(foundRelationship.getOriginUser())
        .setTargetUser(foundRelationship.getTargetUser())
        .setRelationshipType(RelationshipType.FOLLOW);
    relationshipRepository.remove(followRelationship);

    return RelationshipMapper.createResponse(relationshipRepository.remove(foundRelationship));
  }
}
