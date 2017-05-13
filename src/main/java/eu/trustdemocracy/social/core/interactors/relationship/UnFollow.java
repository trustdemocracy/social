package eu.trustdemocracy.social.core.interactors.relationship;

import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;

public class UnFollow implements Interactor<OriginRelationshipRequestDTO, RelationshipResponseDTO> {

  public UnFollow(RelationshipDAO relationshipDAO) {
  }

  @Override
  public RelationshipResponseDTO execute(
      OriginRelationshipRequestDTO originRelationshipRequestDTO) {
    return null;
  }
}
