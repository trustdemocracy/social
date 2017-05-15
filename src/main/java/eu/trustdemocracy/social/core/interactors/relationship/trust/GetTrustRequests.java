package eu.trustdemocracy.social.core.interactors.relationship.trust;

import eu.trustdemocracy.social.core.interactors.Interactor;
import eu.trustdemocracy.social.core.models.request.TargetRelationshipRequestDTO;
import eu.trustdemocracy.social.core.models.response.GetRelationshipsResponseDTO;
import eu.trustdemocracy.social.gateways.RelationshipDAO;

public class GetTrustRequests implements Interactor<TargetRelationshipRequestDTO, GetRelationshipsResponseDTO> {

  public GetTrustRequests(RelationshipDAO relationshipDAO) {
  }

  @Override
  public GetRelationshipsResponseDTO execute(
      TargetRelationshipRequestDTO targetRelationshipRequestDTO) {
    return null;
  }
}
