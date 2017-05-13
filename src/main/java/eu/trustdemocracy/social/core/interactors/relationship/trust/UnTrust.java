package eu.trustdemocracy.social.core.interactors.relationship.trust;

import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.OriginRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;

public class UnTrust implements Interactor<OriginRelationshipRequestDTO, RelationshipResponseDTO> {

  public UnTrust(RelationshipDAO relationshipDAO) {
  }

  @Override
  public RelationshipResponseDTO execute(
      OriginRelationshipRequestDTO originRelationshipRequestDTO) {
    return null;
  }
}
