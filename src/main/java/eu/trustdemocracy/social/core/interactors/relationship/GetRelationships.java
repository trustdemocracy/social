package eu.trustdemocracy.social.core.interactors.relationship;

import eu.trustdemocracy.social.core.entities.util.RelationshipMapper;
import eu.trustdemocracy.social.core.entities.util.UserMapper;
import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetRelationshipsResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;
import lombok.val;

public class GetRelationships implements Interactor<OriginRelationshipRequestDTO, GetRelationshipsResponseDTO> {

  private RelationshipDAO relationshipDAO;

  public GetRelationships(RelationshipDAO relationshipDAO) {
    this.relationshipDAO = relationshipDAO;
  }

  @Override
  public GetRelationshipsResponseDTO execute(OriginRelationshipRequestDTO requestDTO) {
    val user = UserMapper.createEntity(requestDTO.getOriginUserToken());

    val relationships = relationshipDAO.getRelationships(user.getId(), requestDTO.getTargetUserId());

    return RelationshipMapper.createResponse(relationships);
  }
}
