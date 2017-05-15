package eu.trustdemocracy.social.core.interactors.relationship.trust;

import eu.trustdemocracy.social.core.entities.RelationshipStatus;
import eu.trustdemocracy.social.core.entities.RelationshipType;
import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import lombok.val;

public class TrustUser implements Interactor<OriginRelationshipRequestDTO, RelationshipResponseDTO> {

  private RelationshipDAO relationshipDAO;

  public TrustUser(RelationshipDAO relationshipDAO) {
    this.relationshipDAO = relationshipDAO;
  }

  @Override
  public RelationshipResponseDTO execute(
      OriginRelationshipRequestDTO originRelationshipRequestDTO) {
    val relationship = RelationshipMapper.createEntity(originRelationshipRequestDTO);
    relationship.setRelationshipType(RelationshipType.TRUST);
    relationship.setRelationshipStatus(RelationshipStatus.PENDING);
    return RelationshipMapper.createResponse(relationshipDAO.create(relationship));
  }
}
