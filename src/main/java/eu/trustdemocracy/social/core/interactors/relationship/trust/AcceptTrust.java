package eu.trustdemocracy.social.core.interactors.relationship.trust;

import eu.trustdemocracy.social.core.entities.Relationship;
import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import lombok.val;

public class AcceptTrust implements
    Interactor<TargetRelationshipRequestDTO, RelationshipResponseDTO> {

  private RelationshipDAO relationshipDAO;

  public AcceptTrust(RelationshipDAO relationshipDAO) {
    this.relationshipDAO = relationshipDAO;
  }

  @Override
  public RelationshipResponseDTO execute(
      TargetRelationshipRequestDTO targetRelationshipRequestDTO) {
    val relationship = RelationshipMapper.createEntity(targetRelationshipRequestDTO);
    relationship.setRelationshipType(RelationshipType.TRUST);

    val foundRelationship = relationshipDAO.find(relationship);

    if (foundRelationship == null) {
      throw new RuntimeException("The relationship must exist to be accepted");
    }

    val followRelationship = new Relationship()
        .setOriginUser(foundRelationship.getOriginUser())
        .setTargetUser(foundRelationship.getTargetUser())
        .setRelationshipType(RelationshipType.FOLLOW)
        .setRelationshipStatus(RelationshipStatus.ACEPTED);
    relationshipDAO.create(followRelationship);

    foundRelationship.setTargetUser(relationship.getTargetUser());
    foundRelationship.setRelationshipStatus(RelationshipStatus.ACEPTED);
    return RelationshipMapper.createResponse(relationshipDAO.update(foundRelationship));
  }
}
