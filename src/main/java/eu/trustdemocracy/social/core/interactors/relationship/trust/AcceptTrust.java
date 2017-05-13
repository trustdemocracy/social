package eu.trustdemocracy.social.core.interactors.relationship.trust;

import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;

public class AcceptTrust implements
    Interactor<TargetRelationshipRequestDTO, RelationshipResponseDTO> {

  public AcceptTrust(RelationshipDAO relationshipDAO) {
  }

  @Override
  public RelationshipResponseDTO execute(
      TargetRelationshipRequestDTO targetRelationshipRequestDTO) {
    return null;
  }
}
