package eu.trustdemocracy.social.core.interactors.relationship;

import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.RelationshipResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;

public class AcceptFollow implements
    Interactor<TargetRelationshipRequestDTO, RelationshipResponseDTO> {

  public AcceptFollow(RelationshipDAO relationshipDAO) {
  }

  @Override
  public RelationshipResponseDTO execute(
      TargetRelationshipRequestDTO targetRelationshipRequestDTO) {
    return null;
  }
}
