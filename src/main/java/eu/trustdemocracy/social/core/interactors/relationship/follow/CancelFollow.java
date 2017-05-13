package eu.trustdemocracy.social.core.interactors.relationship.follow;

import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import lombok.val;

public class CancelFollow implements
    Interactor<TargetRelationshipRequestDTO, RelationshipResponseDTO> {

  private RelationshipDAO relationshipDAO;

  public CancelFollow(RelationshipDAO relationshipDAO) {
    this.relationshipDAO = relationshipDAO;
  }

  @Override
  public RelationshipResponseDTO execute(
      TargetRelationshipRequestDTO targetRelationshipRequestDTO) {
    val relationship = RelationshipMapper.createEntity(targetRelationshipRequestDTO);
    relationship.setRelationshipType(RelationshipType.FOLLOW);

    val foundRelationship = relationshipDAO.find(relationship);

    if (foundRelationship == null) {
      throw new RuntimeException("The relationship must exist to be removed");
    }

    return RelationshipMapper.createResponse(relationshipDAO.remove(foundRelationship));
  }
}
