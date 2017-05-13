package eu.trustdemocracy.social.core.interactors.relationship;

import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.RelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import lombok.val;

public class FollowUser implements Interactor<RelationshipRequestDTO, RelationshipResponseDTO> {

  private RelationshipDAO relationshipDAO;

  public FollowUser(RelationshipDAO relationshipDAO) {
    this.relationshipDAO = relationshipDAO;
  }

  @Override
  public RelationshipResponseDTO execute(RelationshipRequestDTO relationshipRequestDTO) {
    val relationship = RelationshipMapper.createEntity(relationshipRequestDTO);
    relationship.setRelationshipStatus(RelationshipStatus.PENDING);
    return RelationshipMapper.createResponse(relationshipDAO.create(relationship));
  }
}
